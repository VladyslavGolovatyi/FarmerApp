package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.managers.CsvManager;
import ua.lviv.iot.termPaper.models.Plot;
import ua.lviv.iot.termPaper.services.PlotService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public final class PlotServiceImpl implements PlotService {

    // Сховище ділянок
    public static final Map<Long, Plot> PLOT_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID ділянки
    public static AtomicInteger plotIdHolder = new AtomicInteger();

    @Override
    public void create(final Plot plot) throws IOException {
        final int plotId = plotIdHolder.incrementAndGet();
        plot.setPlotId((long) plotId);
        PLOT_HASH_MAP.put((long) plotId, plot);
        CsvManager.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");
    }

    @Override
    public List<Plot> readAll() {
        return new ArrayList<>(PLOT_HASH_MAP.values());
    }

    @Override
    public Plot read(final long id) {
        return PLOT_HASH_MAP.get(id);
    }

    @Override
    public List<Plot> readAllFarmersPlots(final long id) {
        return PLOT_HASH_MAP.values().stream().filter(plot -> plot.getFarmerId() == id).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Plot plot, final long id) throws IOException {
        CsvManager.deleteFromFile(id, "plot");
        plot.setPlotId(id);
        CsvManager.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");
        if (PLOT_HASH_MAP.containsKey(id)) {
            PLOT_HASH_MAP.put(id, plot);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(final long id) {
        CsvManager.deleteFromFile(id, "plot");
        return PLOT_HASH_MAP.remove(id) != null;
    }
}
