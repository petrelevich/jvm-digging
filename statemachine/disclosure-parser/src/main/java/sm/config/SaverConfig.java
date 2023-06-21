package sm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sm.saver.Saver;
import sm.saver.SaverFile;

@Configuration
public class SaverConfig {

    @Bean
    public Saver saver() {
        return new SaverFile("./disclosure-news.html");
    }
}
