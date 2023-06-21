package sm.parser.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.model.Content;
import sm.model.ListNews;
import sm.parser.ContentParser;
import sm.parser.Parser;

import static sm.parser.simple.State.BEGIN;
import static sm.parser.simple.State.CONT_WRAP;
import static sm.parser.simple.State.LIST_ITEM;
import static sm.parser.simple.State.LIST_ITEM_END;
import static sm.parser.simple.State.TIME;

public class ParserSimple implements Parser {
    private static final Logger log = LoggerFactory.getLogger(ParserSimple.class);


    private State currentState = BEGIN;
    private final ContentParser contentParser = new ContentParser();

    @Override
    public ListNews parse(Content content) {
        log.info("content for parse:{}", content);
        var lines = content.value().split("\n");
        var result = new ListNews();
        for (var line : lines) {
            switch (currentState) {
                case BEGIN -> {
                    if (line.contains("id=\"cont_wrap\"")) {
                        currentState = CONT_WRAP;
                    }
                }
                case CONT_WRAP -> {
                    if (line.contains("class=\"time\"")) {
                        currentState = TIME;
                    }
                    if (line.contains("</div>")) {
                        currentState = BEGIN;
                    }
                }
                case TIME -> {
                    if (line.contains("class=\"listitem\"")) {
                        currentState = LIST_ITEM;
                    }
                    if (line.contains("class=\"date\"")) {
                        var curentNews = result.addAndGetNew();
                        curentNews.date(contentParser.parseDate(line));
                    }
                }
                case LIST_ITEM -> {
                    log.info("newsLine:{}", line);

                    var curentNews = result.getLast().orElseThrow(() -> new IllegalStateException("curentNews can't be null"));

                    curentNews.url(contentParser.parseUrl(line));
                    curentNews.title(contentParser.parseTitle(line));
                    currentState = LIST_ITEM_END;
                }
                case LIST_ITEM_END -> {
                    if (line.contains("</div>")) {
                        currentState = CONT_WRAP;
                    }
                }
            }
        }
        return result;
    }



}
