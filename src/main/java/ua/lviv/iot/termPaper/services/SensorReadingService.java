package ua.lviv.iot.termPaper.services;

import ua.lviv.iot.termPaper.models.SensorReading;

import java.io.IOException;
import java.util.List;

public interface SensorReadingService {

    void create(SensorReading sensorReading) throws IOException;

    List<SensorReading> readAll();

    SensorReading read(long id);

    List<SensorReading> readAllSensorsSensorReadings(long id);

    boolean update(SensorReading sensorReading, long id) throws IOException;

    boolean delete(long id);
}
