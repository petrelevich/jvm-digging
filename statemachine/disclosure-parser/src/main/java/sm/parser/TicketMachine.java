package sm.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketMachine {
    private static final Logger log = LoggerFactory.getLogger(TicketMachine.class);

    public enum CMD {BEGIN, COIN, CANCEL}

    public enum STATE {
        BEGIN(() -> log.info("BEGIN. Внесите монеты")),
        COIN_1(() -> log.info("COIN_1. Внесите еще монеты")),
        COIN_2(() -> log.info("COIN_2. Внесите еще монету")),
        COIN_3(() -> log.info("COIN_3. Получите билет"));
        private final Runnable action;

        STATE(Runnable action) {
            this.action = action;
        }

        public void doAction() {
            action.run();
        }
    }

    private STATE currentState = STATE.BEGIN;

    public TicketMachine() {
        currentState.doAction();
    }

    public void action(CMD cmd) {
        log.info("cmd:{}", cmd);
        switch (currentState) {
            case BEGIN -> {
                if (CMD.COIN == cmd) {
                    currentState = STATE.COIN_1;
                }
                currentState.doAction();
            }
            case COIN_1 -> {
                if (CMD.CANCEL == cmd) {
                    currentState = STATE.BEGIN;
                    log.info("возьмите монету");
                }

                if (CMD.COIN == cmd) {
                    currentState = STATE.COIN_2;
                }
                currentState.doAction();
            }
            case COIN_2 -> {
                if (CMD.CANCEL == cmd) {
                    currentState = STATE.BEGIN;
                    log.info("возьмите 2 монеты");
                }

                if (CMD.COIN == cmd) {
                    currentState = STATE.COIN_3;
                }
                currentState.doAction();
            }
            case COIN_3 -> {
                if (CMD.BEGIN == cmd) {
                    currentState = STATE.BEGIN;
                    currentState.doAction();
                }
            }
        }
    }


    public STATE getCurrentState() {
        return currentState;
    }
}
