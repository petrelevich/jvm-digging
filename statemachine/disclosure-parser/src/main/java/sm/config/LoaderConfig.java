package sm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sm.loader.Loader;
import sm.loader.LoaderUrl;

@Configuration
public class LoaderConfig {

    @Bean
    public Loader loader(@Value("${application.disclosure-url}") String url) {
        return new LoaderUrl(url);
    }
}
