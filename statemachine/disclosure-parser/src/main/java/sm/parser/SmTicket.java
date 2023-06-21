package sm.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmTicket {
    private static final Logger log = LoggerFactory.getLogger(SmTicket.class);

    public enum CMD {BEGIN, COIN, CANCEL}

    public enum STATE {
        BEGIN(() -> log.info("BEGIN. Waiting for coin")),
        COIN_5(() -> log.info("COIN_5. Waiting for one more coin")),
        COIN_10(() -> log.info("COIN_10. Ticket"));
        private final Runnable action;

        STATE(Runnable action) {
            this.action = action;
        }

        public void doAction() {
            action.run();
        }
    }

    private STATE currentState = STATE.BEGIN;

    public SmTicket() {
        currentState.doAction();
    }

    public void action(CMD cmd) {
        log.info("cmd:{}", cmd);
        switch (currentState) {
            case BEGIN -> {
                if (CMD.COIN == cmd) {
                    currentState = STATE.COIN_5;
                }
                currentState.doAction();
            }
            case COIN_5 -> {
                if (CMD.CANCEL == cmd) {
                    currentState = STATE.BEGIN;
                }

                if (CMD.COIN == cmd) {
                    currentState = STATE.COIN_10;
                }
                currentState.doAction();
            }
            case COIN_10 -> {
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
