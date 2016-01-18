package me.cybermaxke.materialfactory.common;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.MixinEnvironment.CompatibilityLevel;
import org.spongepowered.asm.mixin.MixinEnvironment.Side;

public final class MixinEnvironmentAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        final LaunchClassLoader classLoader = Launch.classLoader;

        classLoader.addTransformerExclusion("java.");
        classLoader.addTransformerExclusion("javax.");
        classLoader.addTransformerExclusion("sun.");
        classLoader.addTransformerExclusion("com.sun.");
        classLoader.addTransformerExclusion("com.google.");
        classLoader.addTransformerExclusion("org.apache.");
        classLoader.addTransformerExclusion("net.minecraft.launchwrapper.");
        
        instrumentation.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
					ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
				className = className.replace('/', '.');
				for (String exclusion : classLoader.getTransformerExclusions()) {
					if (className.startsWith(exclusion)) {
						return classfileBuffer;
					}
				}
				
				for (IClassTransformer transformer : classLoader.getTransformers()) {
					byte[] classfileBuffer0 = transformer.transform(null, className, classfileBuffer);
					if (className.equals("org.bukkit.craftbukkit.v1_8_R3.CraftServer")) {
						System.out.println("Transformed the class: " + className + " by "
								+ transformer.getClass().getName() + " " + (classfileBuffer0 != classfileBuffer));
					}
					classfileBuffer = classfileBuffer0;
				}
				return classfileBuffer;
			}
        });

        MixinBootstrap.init();
        MixinEnvironment.setCompatibilityLevel(CompatibilityLevel.JAVA_8);

        MixinEnvironment.getDefaultEnvironment()
                .addConfiguration("mixins.materialfactory.json")
                .setSide(Side.SERVER);
        //MixinEnvironment.init(Phase.DEFAULT);
        System.out.println("Phase: " + MixinEnvironment.getCurrentEnvironment().getPhase());

        Launch.loadTweakers();

        System.out.println("Phase: " + MixinEnvironment.getCurrentEnvironment().getPhase());
    }

    private MixinEnvironmentAgent() {
    }
}
