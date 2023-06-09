package main.examples.repository;

import static org.assertj.core.api.Assertions.assertThat;

import main.examples.model.InfoAdditional;
import main.examples.model.InfoMain;
import main.examples.repository.base.BasePersistenceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class InfoMainRepositoryTest extends BasePersistenceTest {

    @Autowired InfoMainRepository repository;

    @Test
    void saveAndFindTest() {
        var infoAdditional = new InfoAdditional("InfoAdditional");

        var infoMain = new InfoMain("mainData", infoAdditional);

        var infoMainSaved = repository.save(infoMain);
        var infoMainLoaded = repository.findById(infoMainSaved.getInfoMainId());

        assertThat(infoMainLoaded).isPresent();
        assertThat(infoMainLoaded.get().getMainData()).isEqualTo(infoMain.getMainData());

        assertThat(infoMainLoaded.get().getInfoAdditional()).isNotNull();
        assertThat(infoMainLoaded.get().getInfoAdditional().getAdditionalData())
                .isEqualTo(infoAdditional.getAdditionalData());
    }
}
