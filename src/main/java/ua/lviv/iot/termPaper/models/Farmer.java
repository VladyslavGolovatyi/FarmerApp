package ua.lviv.iot.termPaper.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public final class Farmer {
    private Long farmerId;
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
