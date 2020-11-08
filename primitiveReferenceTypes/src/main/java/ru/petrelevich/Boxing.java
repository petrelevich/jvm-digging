package ru.petrelevich;

public class Boxing {
    public static void main(String[] args) {
     //   nullObject();
     //   compareObjects();
          boxUnbox();

    }

    private static void boxUnbox() {
        int x = 1;
        Integer xI1 = x;
        Integer xI2 = Integer.valueOf(x);
        Integer xI3 = Integer.parseInt("1");

        System.out.println("x=" + x);
        System.out.println("xI1=" + xI1);
        System.out.println("xI2=" + xI2);
        System.out.println("xI3=" + xI3);

        int y = xI1.intValue();
        System.out.println("y=" + y);

        Integer xNull = null;
        int xNullable = xNull.intValue();
        System.out.println("xNull" + xNull);
    }

    private static void compareObjects() {
        AnyClass a1 = new AnyClass("a1");
        AnyClass a2 = new AnyClass("a2");

        if (a1 == a2) {
            System.out.println("equal");
        } else {
            System.out.println("NOT equal");
        }

        Integer i1 = Integer.valueOf(1);
        Integer i2 = Integer.valueOf(1);

        if (i1 == i2) {
            System.out.println("equal");
        } else {
            System.out.println("NOT equal");
        }
    }

    private static void nullObject() {
        AnyClass anyClass = null;
        anyClass.method();
    }


    private static class AnyClass {
        final String field;

        public AnyClass(String field) {
            this.field = field;
        }

        static void method() {
            System.out.println("do Nothing");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnyClass anyClass = (AnyClass) o;

            return field != null ? field.equals(anyClass.field) : anyClass.field == null;
        }

        @Override
        public int hashCode() {
            return field != null ? field.hashCode() : 0;
        }
    }
}
