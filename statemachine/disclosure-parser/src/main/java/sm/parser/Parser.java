package sm.parser;

import java.util.List;
import sm.model.Content;
import sm.model.ListNews;
import sm.model.News;

public interface Parser {

    ListNews parse(Content content);
}
