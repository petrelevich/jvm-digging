package sm.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import sm.model.Content;
import sm.parser.simple.ParserSimple;

import static org.assertj.core.api.Assertions.assertThat;

class ParserSimpleTest {

    @Test
    void parse() throws IOException {
        //give
        var parser = new ParserSimple();

        Content content = null;
        try (var stream = ParserSimpleTest.class.getClassLoader().getResourceAsStream("disclosure-news.html")) {
            if (stream != null) {
                try (var isr = new InputStreamReader(stream)) {
                    var contentAsText = new BufferedReader(isr).lines().collect(Collectors.joining(System.lineSeparator()));
                    content = new Content(contentAsText);
                }
            }
        }
        //when
        var newsList = parser.parse(content).getList();

        //then
        assertThat(newsList).hasSize(10);

        var news1 = newsList.get(0);
        assertThat(news1.date()).isEqualTo(LocalDate.of(2023,6,9));
        assertThat(news1.title()).isEqualTo("ЕС готовит правила регулирования ESG-рейтингов для противодействия greenwashing");
        assertThat(news1.url()).isEqualTo("/vse-novosti/novost/5550");

        var news9 = newsList.get(9);
        assertThat(news9.date()).isEqualTo(LocalDate.of(2023,6,1));
        assertThat(news9.title()).isEqualTo("Минэкономразвития предлагает вернуться к законопроекту о нефинансовой отчетности, РСПП за формат рекомендаций");
        assertThat(news9.url()).isEqualTo("/vse-novosti/novost/5541");
    }
}