package instrumentation.setter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author sergey
 * created on 16.07.18.
 */
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader  loader, String className,
                                      Class<?>            classBeingRedefined,
                                      ProtectionDomain    protectionDomain,
                                      byte[]              classfileBuffer) {
                if(className.equals("instrumentation/setter/MyClass")) {
                    return addMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] addMethod(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw){};
        cr.accept(cv, Opcodes.ASM5);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC,  "setValue", "(I)V", null, null);

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ILOAD,1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, "instrumentation/setter/MyClass", "value", "I");

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        byte[] finalClass = cw.toByteArray();

        try(OutputStream fos = new FileOutputStream("setter.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

}
