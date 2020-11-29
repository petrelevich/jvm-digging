package ru.petrelevich.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import ru.petrelevich.model.InfoAdditional;
import ru.petrelevich.model.InfoMain;
import ru.petrelevich.model.SomeObject;
import ru.petrelevich.repository.base.BasePersistenceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InfoMainRepositoryTest extends BasePersistenceTest {

    @Autowired
    InfoMainRepository repository;

    @Test
    void saveAndFindTest() {
        var infoAdditional = new InfoAdditional("InfoAdditional");

        var infoMain = new InfoMain("mainData", infoAdditional);

        var infoMainSaved = repository.save(infoMain);
        var infoMainLoaded = repository.findById(infoMainSaved.getInfoMainId());

        assertThat(infoMainLoaded).isPresent();
        assertThat(infoMainLoaded.get().getMainData()).isEqualTo(infoMain.getMainData());

        assertThat(infoMainLoaded.get().getInfoAdditional()).isNotNull();
        assertThat(infoMainLoaded.get().getInfoAdditional().getAdditionalData()).isEqualTo(infoAdditional.getAdditionalData());
    }
}
