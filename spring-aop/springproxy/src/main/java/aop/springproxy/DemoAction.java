package aop.springproxy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoAction implements CommandLineRunner {
    private final MyClassInterface myClass;

    public DemoAction(MyClassInterface myClass) {
        this.myClass = myClass;
    }

    @Override
    public void run(String... args) {
        myClass.secureAccess("Security Param");
    }
}
