package sm.saver;

import reactor.core.publisher.Mono;
import sm.model.Content;

public interface Saver {
    void save(Content content);
}
