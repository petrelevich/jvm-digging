package sm.loader;

import reactor.core.publisher.Mono;
import sm.model.Content;

public interface Loader {
    Content load();
}
