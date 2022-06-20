package ua.lviv.iot.termPaper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.termPaper.models.Plot;
import ua.lviv.iot.termPaper.services.PlotService;
import ua.lviv.iot.termPaper.servicesImpl.PlotServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
public final class PlotController {

    @Autowired
    private PlotServiceImpl plotService;

    @PostMapping(value = "/plots")
    public ResponseEntity<?> create(@RequestBody final Plot plot) throws IOException {
        plotService.create(plot);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/farmers/{farmerId}/plots")
    public ResponseEntity<List<Plot>> readAllFarmersPlots(@PathVariable(name = "farmerId") final long id) {
        final List<Plot> plots = plotService.readAllFarmersPlots(id);

        return plots != null && !plots.isEmpty()
                ? new ResponseEntity<>(plots, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/plots")
    public ResponseEntity<List<Plot>> readAll() {
        final List<Plot> plots = plotService.readAll();

        return !plots.isEmpty()
                ? new ResponseEntity<>(plots, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/plots/{id}")
    public ResponseEntity<Plot> read(@PathVariable(name = "id") final long id) {
        final Plot plot = plotService.read(id);

        return plot != null
                ? new ResponseEntity<>(plot, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/plots/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") final long id, @RequestBody final Plot plot)
            throws IOException {
        final boolean updated = plotService.update(plot, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/plots/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") final long id) {
        final boolean deleted = plotService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
