package ua.lviv.iot.termPaper.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
public class SensorReading {
    @Id
    @GeneratedValue
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
