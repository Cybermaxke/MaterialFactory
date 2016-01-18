package me.cybermaxke.materialfactory.v18r3;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import com.google.common.collect.Lists;

public final class SMaterialFactoryAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        instrumentation.addTransformer(new Transformer());
        System.out.println("[ItemFactory] Successfully loaded the java agent.");
    }

    private static class Transformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            className = className.replace('/', '.');
            if (className.equals("org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaItem")) {
                return transformCraftMetaItem(classfileBuffer);
            } else if (className.equals("org.bukkit.inventory.ItemStack")) {
                return transformItemStack(classfileBuffer);
            } else if (className.equals("org.bukkit.craftbukkit.v1_8_R3.CraftServer")) {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader,
                        ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                            String[] exceptions) {
                        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
                        if (name.equals("loadPlugins") && desc.equals("()V")) {
                            return new AdviceAdapter(Opcodes.ASM5, visitor, access, name, desc) {
                                @Override
                                protected void onMethodEnter() {
                                    // INVOKESTATIC me/cybermaxke/itemfactory/v18r3/SItemFactory.load ()V
                                    super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                            "me/cybermaxke/itemfactory/v18r3/SItemFactory",
                                            "onLoadPlugins", "()V", false);
                                }
                            };
                        }
                        return visitor;
                    }
                }, ClassReader.EXPAND_FRAMES);
                return classWriter.toByteArray();
            }
            return classfileBuffer;
        }
    }

    private static byte[] transformItemStack(byte[] bytecode) {
        return transformClass(bytecode, IExtendedItemStack.class.getName());
    }

    private static byte[] transformCraftMetaItem(byte[] bytecode) {
        return transformClass(bytecode, IItemData.class.getName());
    }

    private static byte[] transformClass(byte[] bytecode, String theInterface) {
        ClassReader classReader = new ClassReader(bytecode);
        ClassWriter classWriter = new ClassWriter(classReader,
                ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public void visit(int version, int access, String name, String signature,
                    String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, Lists.asList(
                        theInterface.replace('.', '/'), interfaces).toArray(new String[0]));
            }
        }, ClassReader.EXPAND_FRAMES);

        return classWriter.toByteArray();
    }
    
}
