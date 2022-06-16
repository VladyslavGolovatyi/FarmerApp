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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public final class PlotServiceImpl implements PlotService {

    // Сховище ділянок
    private static final Map<Long, Plot> PLOT_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID ділянки
    private static AtomicInteger plotIdHolder = new AtomicInteger();

    public static void init() {
        final int maxNumberOfDaysInMonth = 31;
        for (int i = 1; i <= maxNumberOfDaysInMonth; ++i) {
            Path path = Path.of("plot-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + i + ".csv");
            if (Files.exists(path)) {
                try {
                    Scanner scanner = new Scanner(path);
                    scanner.useDelimiter(",|\\n");
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        Plot plot = new Plot();
                        plot.setPlotId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                        if (plot.getPlotId() > plotIdHolder.get()) {
                            plotIdHolder = new AtomicInteger(Math.toIntExact(plot.getPlotId()));
                        }
                        plot.setFarmerId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                        plot.setArea(Double.parseDouble(scanner.next().replaceAll("\"", "")));
                        plot.setLocation(scanner.next().replaceAll("\"", ""));
                        PLOT_HASH_MAP.put(plot.getPlotId(), plot);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void create(final Plot plot) throws IOException {
        final int plotId = plotIdHolder.incrementAndGet();
        plot.setPlotId((long) plotId);
        PLOT_HASH_MAP.put((long) plotId, plot);
        CsvManager.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");
    }

    @Override
    public List<Plot> readAll() {
        return PLOT_HASH_MAP.values().stream().toList();
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
