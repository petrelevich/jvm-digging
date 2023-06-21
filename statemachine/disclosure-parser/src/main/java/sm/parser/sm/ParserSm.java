package sm.parser.sm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sm.model.Content;
import sm.model.ListNews;
import sm.parser.ContentParser;
import sm.parser.Parser;
import sm.parser.sm.lib.DataSource;
import sm.parser.sm.lib.SmEngin;
import sm.parser.sm.lib.State;


public class ParserSm implements Parser {
    private static final Logger log = LoggerFactory.getLogger(ParserSm.class);
    private final ContentParser contentParser = new ContentParser();

    private final State<String, ListNews> begin = new State<>("begin");
    private final State<String, ListNews> contWrap = new State<>("contWrap");
    private final State<String, ListNews> time = new State<>("time");
    private final State<String, ListNews> listItem = new State<>("listItem");
    private final State<String, ListNews> listItemEnd = new State<>("listItemEnd");

    public ParserSm() {
        begin.setFunctionNextState(line -> {
            if (line.contains("id=\"cont_wrap\"")) {
                return contWrap;
            }
            return null;
        });

        contWrap.setFunctionNextState(line -> {
            if (line.contains("class=\"time\"")) {
                return time;
            }
            if (line.contains("</div>")) {
                return begin;
            }
            return null;
        });

        time.setFunctionNextState(line -> {
            if (line.contains("class=\"listitem\"")) {
                return listItem;
            }
            return null;
        });

        time.setAction((line, currentResult) -> {
            if (line.contains("class=\"date\"")) {
                var news = currentResult.addAndGetNew();
                news.date(contentParser.parseDate(line));
            }
        });

        listItem.setAction(((line, currentResult) -> {
            log.info("newsLine:{}", line);
            var lastNews = currentResult.getLast().orElseThrow(() -> new IllegalStateException("currentNews can't be null"));
            lastNews.url(contentParser.parseUrl(line));
            lastNews.title(contentParser.parseTitle(line));
        }));

        listItem.setFunctionNextState(line -> listItemEnd);

        listItemEnd.setFunctionNextState(line -> {
            if (line.contains("</div>")) {
                return contWrap;
            }
            return null;
        });
    }

    @Override
    public ListNews parse(Content content) {
        SmEngin<String, ListNews> smEngin = new SmEngin<>(begin, new ListNews(), new DataSourceContent(content));
        return smEngin.doJob();
    }

    private static class DataSourceContent implements DataSource<String> {
        private final String[] lines;
        private int currentIdx = 0;

        public DataSourceContent(Content content) {
            lines = content.value().split("\n");
        }

        @Override
        public boolean hasNext() {
            return currentIdx < lines.length;
        }

        @Override
        public String next() {
            return lines[currentIdx++];
        }
    }
}
