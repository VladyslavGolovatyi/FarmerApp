package ua.lviv.iot.termPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.termPaper.models.SensorReading;
import ua.lviv.iot.termPaper.servicesImpl.SensorReadingServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
public final class SensorReadingController {

    @Autowired
    private SensorReadingServiceImpl sensorReadingService;

    @PostMapping(value = "/sensorReadings")
    public ResponseEntity<?> create(@RequestBody final SensorReading sensorReading) throws IOException {
        sensorReadingService.create(sensorReading);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/sensors/{sensorId}/sensorReadings")
    public ResponseEntity<List<SensorReading>> readAllSensorsSensorReadings(@PathVariable(name = "sensorId") final long id) {
        final List<SensorReading> sensorReadings = sensorReadingService.readAllSensorsSensorReadings(id);

        return sensorReadings != null && !sensorReadings.isEmpty()
                ? new ResponseEntity<>(sensorReadings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/sensorReadings")
    public ResponseEntity<List<SensorReading>> readAll() {
        final List<SensorReading> sensorReadings = sensorReadingService.readAll();

        return !sensorReadings.isEmpty()
                ? new ResponseEntity<>(sensorReadings, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/sensorReadings/{id}")
    public ResponseEntity<SensorReading> read(@PathVariable(name = "id") final long id) {
        final SensorReading sensorReading = sensorReadingService.read(id);

        return sensorReading != null
                ? new ResponseEntity<>(sensorReading, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/sensorReadings/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") final long id, @RequestBody final SensorReading sensorReading)
            throws IOException {
        final boolean updated = sensorReadingService.update(sensorReading, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/sensorReadings/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") final long id) {
        final boolean deleted = sensorReadingService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
