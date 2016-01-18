package net.minecraft.launchwrapper;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class Launch {

    public static Map<String, Object> blackboard = new HashMap<>();
    public static LaunchClassLoader classLoader = new LaunchClassLoader((URLClassLoader) ClassLoader.getSystemClassLoader());

    static {
    	blackboard.put("Tweaks", new ArrayList<ITweaker>());
    	blackboard.put("TweakClasses", new ArrayList<String>());
    	blackboard.put("ArgumentList", new ArrayList<String>());
    }

    /**
     * The mixin system uses the tweakers to define in which phase
     * the environment is.
     */
	public static void loadTweakers() {
    	final List<ITweaker> tweakers = (List<ITweaker>) blackboard.get("Tweaks");
    	final List<String> tweakClassNames = (List<String>) blackboard.get("TweakClasses");
    	final List<String> argumentList = (List<String>) blackboard.get("ArgumentList");

		// This is to prevent duplicates - in case a tweaker decides to add itself or something
		final Set<String> allTweakerNames = new HashSet<String>();
    	// The 'definitive' list of tweakers
        final List<ITweaker> allTweakers = new ArrayList<ITweaker>();
        // The game dir (just server root in our case)
        final File gameDir = new File("");
        do {
            for (Iterator<String> it = tweakClassNames.iterator(); it.hasNext(); ) {
                final String tweakName = it.next();
                // Safety check - don't reprocess something we've already visited
                if (allTweakerNames.contains(tweakName)) {
                	// remove the tweaker from the stack otherwise it will create an infinite loop
                    it.remove();
                    continue;
                } else {
                    allTweakerNames.add(tweakName);
                }

                // Ensure we allow the tweak class to load with the parent classloader
                classLoader.addClassLoaderExclusion(tweakName.substring(0, tweakName.lastIndexOf('.')));
                try {
					tweakers.add((ITweaker) Class.forName(tweakName, true, classLoader).newInstance());
				} catch (Exception e) {
					LoggerFactory.getLogger("LaunchWrapper").error("Unable to instantiate the tweaker", e);
				}

                // Remove the tweaker from the list of tweaker names we've processed this pass
                it.remove();
            }

            // Now, iterate all the tweakers we just instantiated
            for (Iterator<ITweaker> it = tweakers.iterator(); it.hasNext();) {
                final ITweaker tweaker = it.next();
                tweaker.acceptOptions(Collections.emptyList(), gameDir, gameDir, "");
                tweaker.injectIntoClassLoader(classLoader);
                allTweakers.add(tweaker);
                // again, remove from the list once we've processed it, so we don't get duplicates
                it.remove();
            }
            // continue around the loop until there's no tweak classes
        } while (!tweakClassNames.isEmpty());

        // Once we're done, we then ask all the tweakers for their arguments and add them all to the
        // master argument list
        for (final ITweaker tweaker : allTweakers) {
            argumentList.addAll(Arrays.asList(tweaker.getLaunchArguments()));
        }
    }
}
