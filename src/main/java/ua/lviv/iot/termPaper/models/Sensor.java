package ua.lviv.iot.termPaper.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Sensor {
    private Long sensorId;
    private Long plotId;
    private String location;
    private SensorType typeOfSensor;

    public String[] receiveHeaders() {
        return new String[]{
                "sensorId",
                "plotId",
                "location",
                "typeOfSensor"
        };
    }

    public String[] toCsv() {
        return new String[]{
                String.valueOf(sensorId),
                String.valueOf(plotId),
                String.valueOf(location),
                typeOfSensor.name()
        };
    }

}
