package sm.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentParser {
    private static final Logger log = LoggerFactory.getLogger(ContentParser.class);

    public LocalDate parseDate(String line) {
        var dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        var patternDate = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
        var matcher = patternDate.matcher(line);
        if (matcher.find()) {
            var dateAsString = matcher.group(0);
            var date = LocalDate.parse(dateAsString, dateFormatter);
            log.info("date:{}", date);
            return date;
        }
        return null;
    }

    public String parseUrl(String line) {
        var patternUrl = Pattern.compile("a href=\"([a-zA-Z0-9/\\-]*)\"");
        var matcher = patternUrl.matcher(line);
        if (matcher.find()) {
            var url = matcher.group(1);
            log.info("url:{}", url);
            return url;
        }
        return null;
    }

    public String parseTitle(String line) {
        var patternTitle = Pattern.compile("\">(.*)</a>");
        var matcher = patternTitle.matcher(line);
        if (matcher.find()) {
            var title = matcher.group(1);
            log.info("title:{}", title);
            return title;
        }
        return null;
    }
}
