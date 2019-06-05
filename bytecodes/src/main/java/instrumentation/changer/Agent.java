package instrumentation.changer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author sergey
 * created on 05.06.19.
 */
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("instrumentation/changer/AnyClass")) {
                    return changeMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] changeMethod(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM7, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (name.equals("summator")) {
                    return new ChangeMethodVisitor(Opcodes.ASM7, methodVisitor, access, name, descriptor);
                } else {
                    return methodVisitor;
                }
            }
        };
        cr.accept(cv, Opcodes.ASM7);

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxy.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

    private static class ChangeMethodVisitor extends AdviceAdapter {
        ChangeMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor);
        }

        @Override
        public void visitInsn(final int opcode) {
            if (IADD == opcode) {
                System.out.println("replace IADD to ISUB");
                super.visitInsn(ISUB);
            } else {
                super.visitInsn(opcode);
            }

        }
    }
}
