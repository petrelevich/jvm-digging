package main.demo.serivce;

import main.demo.model.Dog;
import main.demo.model.Owner;
import main.demo.repository.DogRepository;
import main.demo.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;


@Service("dogService")
public class DogService {
    private static final Logger log = LoggerFactory.getLogger(DogService.class);

    private final DogRepository dogRepository;
    private final OwnerRepository ownerRepository;

    public DogService(DogRepository dogRepository, OwnerRepository ownerRepository) {
        this.dogRepository = dogRepository;
        this.ownerRepository = ownerRepository;
    }

    public void life() {
        level1();
        level2();
        level3();
    }

    private void level1() {
        String dogName = "Шарик";
        log.info("creating a new dog:{}", dogName);
        var dog = new Dog(dogName);
        var dogCreated = dogRepository.save(dog);
        log.info("created a new dog:{}", dogCreated);

        var dogById = dogRepository.findById(dogCreated.getDogId())
                .orElseThrow(() -> new RuntimeException("dog not found, DogId: " + dogCreated.getDogId()));

        log.info("selected dog by id:{}", dogById);

        var dogsByName = dogRepository.findByName(dogName);

        log.info("selected dog by Name:{}", dogsByName);

        var dog1 = dogRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("dog not found, id: " + 1));
        var dog1Renamed = new Dog(dog1.getDogId(),
                String.valueOf(System.currentTimeMillis()), dog1.getOwnerName());
        dogRepository.save(dog1Renamed);

        var dog1Selected = dogRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("dog not found, id: " + 1));

        log.info("dog #1 :{}", dog1Selected);
    }

    private void level2() {
        var ownerName = String.format("Вася:%d", System.currentTimeMillis());
        var owner = new Owner(ownerName, "Васильки", Collections.emptySet(), true);
        ownerRepository.save(owner);
        var selectedOwner = ownerRepository.findById(ownerName)
                .orElseThrow(() -> new RuntimeException("selected owner not found, name: " + ownerName));
        log.info("selectedOwner:{}", selectedOwner);

        var ownerReal = new Owner(ownerName, "Васильки",
                Set.of(new Dog("Бобик", null),
                        new Dog("Барбос")), false);

        ownerRepository.save(ownerReal);

        var selectedOwnerReal = ownerRepository.findById(ownerName)
                .orElseThrow(() -> new RuntimeException("owner not found, name: " + ownerName));
        log.info("selectedOwnerReal:{}", selectedOwnerReal);
        log.info("selectedOwnerReal, dogs:{}", selectedOwnerReal.getDogs());

        ownerRepository.updateAddress(ownerName, "новый адрес");
        var selectedOwnerRealAddress = ownerRepository.findById(ownerName)
                .orElseThrow(() -> new RuntimeException("owner not found, name: " + ownerName));
        log.info("selectedOwnerRealAddress:{}", selectedOwnerRealAddress);
        log.info("selectedOwnerRealAddress, getAddress:{}", selectedOwnerRealAddress.getAddress());
    }

    private void level3() {
        String dogName = String.format("Мухтар:%d", System.currentTimeMillis());
        log.info("creating a new guard dog:{}", dogName);
        var dog = new Dog(dogName);
        var savedDog = dogRepository.save(dog);
        log.info("saved a new guard dog:{}", savedDog);

        var guardDog = dogRepository.findGuardDogByName(dogName)
                .orElseThrow(() -> new RuntimeException("guard Dog not found, name:" + dogName));

        log.info("guard dog:{}", guardDog);
    }

}
