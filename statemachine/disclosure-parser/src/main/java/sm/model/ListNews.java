package sm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListNews {
    private final List<News> list = new ArrayList<>();

    public List<News> getList() {
        return list;
    }

    public News addAndGetNew() {
        var news = new News();
        list.add(news);
        return news;
    }

    public Optional<News> getLast() {
        return Optional.ofNullable(list.get(list.size() - 1));
    }
}
