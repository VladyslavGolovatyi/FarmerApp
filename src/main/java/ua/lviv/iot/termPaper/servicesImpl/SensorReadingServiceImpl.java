package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.models.SensorReading;
import ua.lviv.iot.termPaper.services.SensorReadingService;
import ua.lviv.iot.termPaper.storage.SensorReadingStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public final class SensorReadingServiceImpl implements SensorReadingService {

    @Autowired
    private SensorReadingStorage sensorReadingStorage;

    @Override
    public void create(final SensorReading sensorReading) throws IOException {
        sensorReadingStorage.addSensorReadingToHashMap(sensorReading);
        sensorReadingStorage.writeToFile(sensorReading.receiveHeaders(), sensorReading.toCsv(), "sensorReading");
    }

    @Override
    public List<SensorReading> readAll() {
        return sensorReadingStorage.getAllSensorReadings();
    }

    @Override
    public SensorReading read(final long id) {
        return sensorReadingStorage.getSensorReadingById(id);
    }

    @Override
    public List<SensorReading> readAllSensorsSensorReadings(final long sensorId) {
        return sensorReadingStorage.getAllSensorsSensorReadings(sensorId);
    }

    @Override
    public boolean update(final SensorReading sensorReading, final long id) throws IOException {
        sensorReadingStorage.deleteFromFile(id, "sensorReading");
        sensorReading.setSensorReadingId(id);
        sensorReadingStorage.writeToFile(sensorReading.receiveHeaders(), sensorReading.toCsv(), "sensorReading");
        return sensorReadingStorage.updateHashMap(id, sensorReading);
    }

    @Override
    public boolean delete(final long id) {
        sensorReadingStorage.deleteFromFile(id, "sensorReading");
        return sensorReadingStorage.deleteFromHashMap(id);
    }

    @PostConstruct
    private void loadSensorReadings() {
        sensorReadingStorage.load("sensorReading");
    }
}
