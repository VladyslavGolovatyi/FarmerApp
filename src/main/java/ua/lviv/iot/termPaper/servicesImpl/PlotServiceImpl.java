package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.models.Plot;
import ua.lviv.iot.termPaper.services.PlotService;
import ua.lviv.iot.termPaper.storage.PlotStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public final class PlotServiceImpl implements PlotService {

    @Autowired
    private PlotStorage plotStorage;

    @Override
    public void create(final Plot plot) throws IOException {
        plotStorage.addPlotToHashMap(plot);
        plotStorage.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");
    }

    @Override
    public List<Plot> readAll() {
        return plotStorage.getAllPlots();
    }

    @Override
    public Plot read(final long id) {
        return plotStorage.getPlotById(id);
    }

    @Override
    public List<Plot> readAllFarmersPlots(final long farmerId) {
        return plotStorage.getAllFarmersPlots(farmerId);
    }

    @Override
    public boolean update(final Plot plot, final long id) throws IOException {
        plotStorage.deleteFromFile(id, "plot");
        plot.setPlotId(id);
        plotStorage.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");
        return plotStorage.updateHashMap(id, plot);
    }

    @Override
    public boolean delete(final long id) {
        plotStorage.deleteFromFile(id, "plot");
        return plotStorage.deleteFromHashMap(id);
    }

    @PostConstruct
    private void loadPlots() {
        plotStorage.load("plot");
    }
}
