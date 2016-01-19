package me.cybermaxke.materialfactory.theplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import me.cybermaxke.materialfactory.common.asm.ClassRenameVisitor;

import com.google.common.io.ByteStreams;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;

public final class MaterialFactoryPluginInjector {

	public static void inject(File pluginsFolder) {
		if (!pluginsFolder.exists()) {
			pluginsFolder.mkdirs();
        }

        try {
            final File tempPluginFile = new File(pluginsFolder, "temp_itemfactoryplugin.jar");
            if (tempPluginFile.exists()) {
                tempPluginFile.delete();
            }
            tempPluginFile.deleteOnExit();

            final String pluginName = "me/cybermaxke/materialfactory/plugin/MaterialFactoryPlugin";
            ClassReader classReader = new ClassReader(ByteStreams.toByteArray(MaterialFactoryPluginInjector.class
                    .getResourceAsStream("/me/cybermaxke/itemfactory/theplugin/MaterialFactoryPlugin.class")));
            ClassWriter classWriter = new ClassWriter(classReader,
                    ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classReader.accept(new ClassRenameVisitor(classWriter, pluginName), ClassReader.EXPAND_FRAMES);

            final JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempPluginFile));
            jos.putNextEntry(new JarEntry(pluginName + ".class"));
            jos.write(classWriter.toByteArray());
            jos.closeEntry();
            jos.putNextEntry(new JarEntry("plugin.yml"));
            jos.write(ByteStreams.toByteArray(MaterialFactoryPluginInjector.class
            		.getResourceAsStream("/" + "thePlugin.yml")));
            jos.closeEntry();
            jos.flush();
            jos.close();
        } catch (Exception e) {
            System.out.println("[ItemFactory] An error occurred while loading the plugin:");
            e.printStackTrace();
        }
	}
}
