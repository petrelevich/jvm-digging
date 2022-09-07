package ru.petrelevich.generics.animals;

public class Dog implements Runner, Heroic {
    private final String name;

    public Dog(String name) {
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
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
