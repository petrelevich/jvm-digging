package sm.parser.sm.lib;

public interface DataSource<T> {
    boolean hasNext();

    T next();
}
