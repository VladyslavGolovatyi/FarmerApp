package ua.lviv.iot.termPaper.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@EqualsAndHashCode
public class SensorReading {
    private Long sensorReadingId;
    private Long sensorId;
    private LocalDateTime dateTime;
    private double reading;

    public String[] receiveHeaders() {
        return new String[]{
                "sensorReadingId",
                "sensorId",
                "dateTime",
                "reading"
        };
    }

    public String[] toCsv() {
        return new String[]{
                String.valueOf(sensorReadingId),
                String.valueOf(sensorId),
                dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                String.valueOf(reading)
        };
    }
}
