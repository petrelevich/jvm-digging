package main.demo.model;

public class GuardDog {
    private final Long dogId;
    private final String name;
    private final String officerName;

    public GuardDog(Long dogId, String name, String officerName) {
        this.dogId = dogId;
        this.name = name;
        this.officerName = officerName;
    }

    public Long getDogId() {
        return dogId;
    }

    public String getName() {
        return name;
    }

    public String getOfficerName() {
        return officerName;
    }

    @Override
    public String toString() {
        return "GuardDog{" +
                "dogId=" + dogId +
                ", name='" + name + '\'' +
                ", officerName='" + officerName + '\'' +
                '}';
    }
}
