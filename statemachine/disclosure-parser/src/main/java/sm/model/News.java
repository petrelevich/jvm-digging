package sm.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class News {
    LocalDate date;
    String url;
    String title;
}
