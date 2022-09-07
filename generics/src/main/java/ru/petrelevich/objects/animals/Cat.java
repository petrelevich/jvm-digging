package ru.petrelevich.objects.animals;

public class Cat implements Runner, Heroic {
    private final String name;

    public Cat(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " runs");
    }

    @Override
    public void feat() {
        System.out.println(name + " performs a feat");
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                '}';
    }
}
