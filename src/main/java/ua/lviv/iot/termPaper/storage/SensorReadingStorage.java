package ua.lviv.iot.termPaper.storage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.termPaper.models.SensorReading;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class SensorReadingStorage extends AbstractStorage {

    private final Map<Long, SensorReading> sensorReadings = new HashMap<>();

    private Long nextAvailableId = 1L;

    @Override
    public Long getNextAvailableId() {
        return nextAvailableId;
    }

    public void addSensorReadingToHashMap(final SensorReading sensorReading) {
        sensorReading.setSensorReadingId(nextAvailableId++);
        sensorReadings.put(sensorReading.getSensorReadingId(), sensorReading);
    }

    public List<SensorReading> getAllSensorReadings() {
        return new ArrayList<>(sensorReadings.values());
    }

    public List<SensorReading> getAllSensorsSensorReadings(final long sensorId) {
        return sensorReadings.values().stream().filter(sensorReading -> sensorReading.getSensorId() == sensorId).collect(Collectors.toList());
    }

    public SensorReading getSensorReadingById(final Long id) {
        return sensorReadings.get(id);
    }

    public boolean deleteFromHashMap(final Long id) {
        return sensorReadings.remove(id) != null;
    }

    public boolean updateHashMap(final long id, final SensorReading sensorReading) {
        if (!sensorReadings.containsKey(id)) {
            return false;
        } else {
            sensorReadings.put(id, sensorReading);
        }
        return true;
    }

    @Override
    protected void fromCsvToRecord(final String[] values) {
        SensorReading sensorReading = new SensorReading();
        sensorReading.setSensorReadingId(Long.valueOf(values[0].replaceAll("\"", "")));
        if (sensorReading.getSensorReadingId() > nextAvailableId) {
            nextAvailableId = sensorReading.getSensorReadingId() + 1;
        }
        sensorReading.setSensorId(Long.valueOf(values[1].replaceAll("\"", "")));
        sensorReading.setDateTime(LocalDateTime.parse(values[2].replaceAll("\"", "")));
        sensorReading.setReading(Double.parseDouble(values[3].replaceAll("\"", "")));
        sensorReadings.put(sensorReading.getSensorReadingId(), sensorReading);
    }

}
