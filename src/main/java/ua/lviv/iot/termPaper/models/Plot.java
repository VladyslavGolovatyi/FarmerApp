package ua.lviv.iot.termPaper.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public final class Plot {

    private Long plotId;
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
