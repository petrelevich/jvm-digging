package sm.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

class TicketMachineTest {

    @Test
    void actionOk() {
        //given
        var sm = new TicketMachine();

        //when
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.COIN_1);

        //when
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.BEGIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.COIN_3);

        //when
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.BEGIN);
    }

    @Test
    void actionCancel() {
        //given
        var sm = new TicketMachine();

        //when
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.COIN_1);

        //when
        sm.action(TicketMachine.CMD.CANCEL);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.BEGIN);

        //when
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.CANCEL);
        sm.action(TicketMachine.CMD.CANCEL);
        sm.action(TicketMachine.CMD.CANCEL);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.COIN_3);

        //when
        sm.action(TicketMachine.CMD.BEGIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.COIN);
        sm.action(TicketMachine.CMD.CANCEL);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(TicketMachine.STATE.BEGIN);
    }
}