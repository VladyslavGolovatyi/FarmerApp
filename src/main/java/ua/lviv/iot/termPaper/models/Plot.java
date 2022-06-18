package ua.lviv.iot.termPaper.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public final class Plot {

    private @Id
    @GeneratedValue Long plotId;
    private Long farmerId;
    private double area;
    private String location;

    public String[] receiveHeaders() {
        return new String[]{
                "plotId",
                "farmerId",
                "area",
                "location"
        };
    }

    public String[] toCsv() {
        return new String[]{
                String.valueOf(plotId),
                String.valueOf(farmerId),
                String.valueOf(area),
                location
        };
    }
}
