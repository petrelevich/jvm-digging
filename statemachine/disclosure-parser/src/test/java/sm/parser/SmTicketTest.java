package sm.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

class SmTicketTest {

    @Test
    void actionOk() {
        //given
        var sm = new SmTicket();

        //when
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.COIN_5);

        //when
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.BEGIN);
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.COIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.COIN_10);

        //when
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.BEGIN);
    }

    @Test
    void actionCancel() {
        //given
        var sm = new SmTicket();

        //when
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.BEGIN);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.COIN_5);

        //when
        sm.action(SmTicket.CMD.CANCEL);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.BEGIN);

        //when
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.COIN);
        sm.action(SmTicket.CMD.CANCEL);
        //then
        assertThat(sm.getCurrentState()).isEqualTo(SmTicket.STATE.COIN_10);
    }
}