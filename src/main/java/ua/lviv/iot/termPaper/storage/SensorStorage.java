package ua.lviv.iot.termPaper.storage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.termPaper.models.Sensor;
import ua.lviv.iot.termPaper.models.SensorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class SensorStorage extends AbstractStorage {

    private final Map<Long, Sensor> sensors = new HashMap<>();

    private Long nextAvailableId = 1L;

    @Override
    public Long getNextAvailableId() {
        return nextAvailableId;
    }

    public void addSensorToHashMap(final Sensor sensor) {
        sensor.setSensorId(nextAvailableId++);
        sensors.put(sensor.getSensorId(), sensor);
    }

    public List<Sensor> getAllSensors() {
        return new ArrayList<>(sensors.values());
    }

    public List<Sensor> getAllPlotsSensors(final long plotId) {
        return sensors.values().stream().filter(sensor -> sensor.getPlotId() == plotId).collect(Collectors.toList());
    }

    public Sensor getSensorById(final Long id) {
        return sensors.get(id);
    }

    public boolean deleteFromHashMap(final Long id) {
        return sensors.remove(id) != null;
    }

    public boolean updateHashMap(final long id, final Sensor sensor) {
        if (!sensors.containsKey(id)) {
            return false;
        } else {
            sensors.put(id, sensor);
        }
        return true;
    }

    @Override
    protected void fromCsvToRecord(final String[] values) {
        Sensor sensor = new Sensor();
        sensor.setSensorId(Long.valueOf(values[0].replaceAll("\"", "")));
        if (sensor.getSensorId() > nextAvailableId) {
            nextAvailableId = sensor.getSensorId() + 1;
        }
        sensor.setPlotId(Long.valueOf(values[1].replaceAll("\"", "")));
        sensor.setLocation(values[2].replaceAll("\"", "").replaceAll("\r", ""));
        sensor.setTypeOfSensor(SensorType.valueOf(values[3].replaceAll("\"", "").trim()));
        sensors.put(sensor.getSensorId(), sensor);
    }

}
