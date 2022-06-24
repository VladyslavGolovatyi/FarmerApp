package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.models.Sensor;
import ua.lviv.iot.termPaper.services.SensorService;
import ua.lviv.iot.termPaper.storage.SensorStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public final class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorStorage sensorStorage;

    @Override
    public void create(final Sensor sensor) throws IOException {
        sensorStorage.addSensorToHashMap(sensor);
        sensorStorage.writeToFile(sensor.receiveHeaders(), sensor.toCsv(), "sensor");
    }

    @Override
    public List<Sensor> readAll() {
        return sensorStorage.getAllSensors();
    }

    @Override
    public Sensor read(final long id) {
        return sensorStorage.getSensorById(id);
    }

    @Override
    public List<Sensor> readAllPlotsSensors(final long farmerId) {
        return sensorStorage.getAllPlotsSensors(farmerId);
    }

    @Override
    public boolean update(final Sensor sensor, final long id) throws IOException {
        sensorStorage.deleteFromFile(id, "sensor");
        sensor.setSensorId(id);
        sensorStorage.writeToFile(sensor.receiveHeaders(), sensor.toCsv(), "sensor");
        return sensorStorage.updateHashMap(id, sensor);
    }

    @Override
    public boolean delete(final long id) {
        sensorStorage.deleteFromFile(id, "sensor");
        return sensorStorage.deleteFromHashMap(id);
    }

    @PostConstruct
    private void loadSensors() {
        sensorStorage.load("sensor");
    }
}
