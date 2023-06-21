package sm.parser.sm.lib;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class State<T, R> {
    private final String name;
    private Function<T, State<T, R>> functionNextState = null;
    private BiConsumer<T, R> action = null;

    public State(String name) {
        this.name = name;
    }

    public void setFunctionNextState(Function<T, State<T, R>> functionNextState) {
        this.functionNextState = functionNextState;
    }

    public void setAction(BiConsumer<T, R> action) {
        this.action = action;
    }

    public Optional<State<T, R>> nextState(T inData) {
        return Optional.ofNullable(functionNextState.apply(inData));
    }

    public void doAction(T inData, R result) {
        if (action != null) {
            action.accept(inData, result);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                '}';
    }
}
