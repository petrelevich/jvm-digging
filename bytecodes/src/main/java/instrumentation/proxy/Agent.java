package instrumentation.proxy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

/**
 * @author sergey
 * created on 24.04.19.
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
                if (className.equals("instrumentation/proxy/MyClassImpl")) {
                    return addProxyMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (name.equals("secureAccess")) {
                    return super.visitMethod(access, "secureAccessProxied", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "secureAccess", "(Ljava/lang/String;)V", null, null);

        Handle handle = new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", handle, "logged param:\u0001");

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "instrumentation/proxy/MyClassImpl", "secureAccessProxied", "(Ljava/lang/String;)V", false);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();


        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxy.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

}
