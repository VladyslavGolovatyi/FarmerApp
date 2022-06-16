package ua.lviv.iot.termPaper.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public final class Farmer {
    private @Id
    @GeneratedValue Long farmerId;
    private String fullName;

    public String[] receiveHeaders() {
        return new String[]{
                "farmerId",
                "fullName"
        };
    }

    public String[] toCsv() {
        return new String[]{
                String.valueOf(farmerId),
                fullName
        };
    }
}
