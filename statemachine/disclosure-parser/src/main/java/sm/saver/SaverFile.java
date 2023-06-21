package sm.saver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.model.Content;

public class SaverFile implements Saver {
    private static final Logger log = LoggerFactory.getLogger(SaverFile.class);

    private final Path pathForSave;

    public SaverFile(String fileName) {
        pathForSave = Path.of(fileName);
    }

    @Override
    public void save(Content content) {
        log.info("save to the file:{}", pathForSave);
        var value = content.value();
        try {
            Files.writeString(pathForSave, value);
        } catch (IOException e) {
            log.error("pathForSave:{}", pathForSave, e);
            throw new RuntimeException(e);
        }
    }
}
