package main.examples.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("info_additional")
public class InfoAdditional {

    @Id private final Long infoAdditionalId;
    private final Long infoMainId;
    private final String additionalData;

    public InfoAdditional(String additionalData) {
        this.infoAdditionalId = null;
        this.infoMainId = null;
        this.additionalData = additionalData;
    }

    @PersistenceCreator
    private InfoAdditional(Long infoAdditionalId, Long infoMainId, String additionalData) {
        this.infoAdditionalId = infoAdditionalId;
        this.infoMainId = infoMainId;
        this.additionalData = additionalData;
    }

    public Long getInfoAdditionalId() {
        return infoAdditionalId;
    }

    public Long getInfoMainId() {
        return infoMainId;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    @Override
    public String toString() {
        return "InfoAdditional{"
                + "infoAdditionalId="
                + infoAdditionalId
                + ", infoMainId="
                + infoMainId
                + ", additionalData='"
                + additionalData
                + '\''
                + '}';
    }
}
