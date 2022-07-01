package ua.lviv.iot.termPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.termPaper.models.Sensor;
import ua.lviv.iot.termPaper.servicesImpl.SensorServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
public final class SensorController {

    @Autowired
    private SensorServiceImpl sensorService;

    @PostMapping(value = "/sensors")
    public ResponseEntity<?> create(@RequestBody final Sensor sensor) throws IOException {
        sensorService.create(sensor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/plots/{plotId}/sensors")
    public ResponseEntity<List<Sensor>> readAllPlotsSensors(@PathVariable(name = "plotId") final long id) {
        final List<Sensor> sensors = sensorService.readAllPlotsSensors(id);

        return sensors != null && !sensors.isEmpty()
                ? new ResponseEntity<>(sensors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/sensors")
    public ResponseEntity<List<Sensor>> readAll() {
        final List<Sensor> sensors = sensorService.readAll();

        return !sensors.isEmpty()
                ? new ResponseEntity<>(sensors, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/sensors/{id}")
    public ResponseEntity<Sensor> read(@PathVariable(name = "id") final long id) {
        final Sensor sensor = sensorService.read(id);

        return sensor != null
                ? new ResponseEntity<>(sensor, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/sensors/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") final long id, @RequestBody final Sensor sensor)
            throws IOException {
        final boolean updated = sensorService.update(sensor, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/sensors/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") final long id) {
        final boolean deleted = sensorService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
