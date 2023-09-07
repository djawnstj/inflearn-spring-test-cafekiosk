package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    public void getName() throws Exception {
        // given
        final Americano americano = new Americano();

        // when

        // then
        assertEquals(americano.getName(), "아메리카노");
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @Test
    public void getPrice() throws Exception {
        // given
        final Americano americano = new Americano();

        // when

        // then
        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}