package ua.lviv.iot.termPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.termPaper.models.Farmer;
import ua.lviv.iot.termPaper.services.FarmerService;
import ua.lviv.iot.termPaper.servicesImpl.FarmerServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
public final class FarmerController {

    @Autowired
    private FarmerServiceImpl farmerService;

    @PostMapping(value = "/farmers")
    public ResponseEntity<?> create(@RequestBody final Farmer farmer) throws IOException {
        farmerService.create(farmer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/farmers")
    public ResponseEntity<List<Farmer>> read() {
        final List<Farmer> farmers = farmerService.readAll();

        return !farmers.isEmpty()
                ? new ResponseEntity<>(farmers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/farmers/{id}")
    public ResponseEntity<Farmer> read(@PathVariable(name = "id") final int id) {
        final Farmer farmer = farmerService.read(id);

        return farmer != null
                ? new ResponseEntity<>(farmer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/farmers/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") final int id, @RequestBody final Farmer farmer)
            throws IOException {
        final boolean updated = farmerService.update(farmer, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/farmers/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") final int id) {
        final boolean deleted = farmerService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
