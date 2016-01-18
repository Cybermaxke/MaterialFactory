package me.cybermaxke.materialfactory.common.asm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class ClassRenameVisitor extends ClassVisitor {

    private Set<String> oldNames = new HashSet<>();
    private final String newName;

    public ClassRenameVisitor(ClassVisitor cv, String newName) {
        super(Opcodes.ASM5, cv);
        this.newName = newName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.oldNames.add(name);
        super.visit(version, Opcodes.ACC_PUBLIC, this.newName, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, fix(desc), fix(signature), exceptions);
        if (mv != null && (access & Opcodes.ACC_ABSTRACT) == 0) {
            mv = new MethodRenamer(mv);
        }
        return mv;
    }

    class MethodRenamer extends MethodVisitor {

        public MethodRenamer(final MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitTypeInsn(int i, String s) {
            super.visitTypeInsn(i, oldNames.contains(s) ? newName : s);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            super.visitFieldInsn(opcode, oldNames.contains(owner) ? newName : owner, name, fix(desc));
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean inf) {
            super.visitMethodInsn(opcode, oldNames.contains(owner) ? newName : owner, name, fix(desc), inf);
        }
    }

    private String fix(String desc) {
        if (desc != null) {
            Iterator<String> it = this.oldNames.iterator();
            String name;
            while (it.hasNext()) {
                name = it.next();
                if (desc.indexOf(name) != -1) {
                    desc = desc.replaceAll(name, this.newName);
                }
            }
        }
        return desc;
    }
}
