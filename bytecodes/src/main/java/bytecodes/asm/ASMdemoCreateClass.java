package bytecodes.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;


/*
javac HelloWorld.java
javap -v HelloWorld.class
javap -private HelloWorld.class
*/

public class ASMdemoCreateClass {

    public static void main(String[] args) throws Exception {
        var className = "HelloWorld";

        //Генератор классов
        var cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        //Конструктор класса
        MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();

        //Вызываем конструктор предка (Object)
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(0, 0);
        constructor.visitEnd();

        //Создаем метод printHi
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "printHi", "()V", null, null);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello, World!");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        //Загружаем полученный класс
        Class<?> helloWorldClass = new MyClassLoader().defineClass(className, cw.toByteArray());

        //Выполняем метод main
        var method = helloWorldClass.getMethod("printHi");
        method.invoke(null);

        try (OutputStream fos = new FileOutputStream("HelloWorld.class")) {
            fos.write(cw.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Самодельный ClassLoader
    public static class MyClassLoader extends ClassLoader {
        Class<?> defineClass(String className, byte[] bytecode) {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    }
}
