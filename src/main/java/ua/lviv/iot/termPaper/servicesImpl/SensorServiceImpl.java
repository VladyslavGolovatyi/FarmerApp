package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.managers.CsvManager;
import ua.lviv.iot.termPaper.models.Sensor;
import ua.lviv.iot.termPaper.models.SensorType;
import ua.lviv.iot.termPaper.services.SensorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public final class SensorServiceImpl implements SensorService {

    // Сховище сенсорів
    public static final Map<Long, Sensor> SENSOR_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID сенсора
    public static AtomicInteger sensorIdHolder = new AtomicInteger();

    @Override
    public void create(final Sensor sensor) throws IOException {
        final int sensorId = sensorIdHolder.incrementAndGet();
        sensor.setSensorId((long) sensorId);
        SENSOR_HASH_MAP.put((long) sensorId, sensor);
        CsvManager.writeToFile(sensor.receiveHeaders(), sensor.toCsv(), "sensor");
    }

    @Override
    public List<Sensor> readAll() {
        return new ArrayList<>(SENSOR_HASH_MAP.values());
    }

    @Override
    public Sensor read(final long id) {
        return SENSOR_HASH_MAP.get(id);
    }

    @Override
    public List<Sensor> readAllPlotsSensors(final long id) {
        return SENSOR_HASH_MAP.values().stream().filter(sensor -> sensor.getPlotId() == id).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Sensor sensor, final long id) throws IOException {
        CsvManager.deleteFromFile(id, "sensor");
        sensor.setSensorId(id);
        CsvManager.writeToFile(sensor.receiveHeaders(), sensor.toCsv(), "sensor");
        if (SENSOR_HASH_MAP.containsKey(id)) {
            SENSOR_HASH_MAP.put(id, sensor);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(final long id) {
        CsvManager.deleteFromFile(id, "sensor");
        return SENSOR_HASH_MAP.remove(id) != null;
    }
}
