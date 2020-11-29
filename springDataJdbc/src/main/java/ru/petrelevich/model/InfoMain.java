package ru.petrelevich.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("info_main")
public class InfoMain {

    @Id
    private final Long infoMainId;
    private final String mainData;

    @MappedCollection(idColumn = "info_main_id")
    private final InfoAdditional infoAdditional;

    public InfoMain(String mainData, InfoAdditional infoAdditional) {
        this.infoMainId = null;
        this.mainData = mainData;
        this.infoAdditional = infoAdditional;
    }


    @PersistenceConstructor
    private InfoMain(Long infoMainId, String mainData, InfoAdditional infoAdditional) {
        this.infoMainId = infoMainId;
        this.mainData = mainData;
        this.infoAdditional = infoAdditional;
    }

    public Long getInfoMainId() {
        return infoMainId;
    }

    public String getMainData() {
        return mainData;
    }

    public InfoAdditional getInfoAdditional() {
        return infoAdditional;
    }

    @Override
    public String toString() {
        return "InfoMain{" +
                "infoMainId=" + infoMainId +
                ", mainData='" + mainData + '\'' +
                ", infoAdditional=" + infoAdditional +
                '}';
    }
}
