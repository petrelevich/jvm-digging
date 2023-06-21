package sm.parser.sm.lib;

public class SmEngin<T, R> {
    private State<T, R> currentState;
    private final R result;
    private final DataSource<T> dataSource;

    public SmEngin(State<T, R> initialState, R result, DataSource<T> dataSource) {
        this.currentState = initialState;
        this.result = result;
        this.dataSource = dataSource;
    }

    public R doJob() {
        while (dataSource.hasNext()) {
            var inData = dataSource.next();
            currentState.doAction(inData, result);
            currentState = currentState.nextState(inData).orElse(currentState);
        }
        return result;
    }
}
