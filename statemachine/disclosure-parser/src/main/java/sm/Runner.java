package sm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sm.loader.Loader;
import sm.saver.Saver;

@Component
public class Runner implements CommandLineRunner {
    private final Loader loader;
    private final Saver saver;

    public Runner(Loader loader, Saver saver) {
        this.loader = loader;
        this.saver = saver;
    }

    @Override
    public void run(String... args) throws Exception {
        saver.save(loader.load());
    }
}
