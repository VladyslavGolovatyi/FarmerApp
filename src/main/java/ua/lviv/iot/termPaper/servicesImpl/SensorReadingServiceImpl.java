package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.managers.CsvManager;
import ua.lviv.iot.termPaper.models.SensorReading;
import ua.lviv.iot.termPaper.services.SensorReadingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public final class SensorReadingServiceImpl implements SensorReadingService {

    // Сховище ділянок
    private static final Map<Long, SensorReading> PLOT_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID ділянки
    private static AtomicInteger sensorReadingIdHolder = new AtomicInteger();

    public static void init() {
        final int maxNumberOfDaysInMonth = 31;
        for (int i = 1; i <= maxNumberOfDaysInMonth; ++i) {
            Path path = Path.of("sensorReading-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + i + ".csv");
            if (Files.exists(path)) {
                try {
                    Scanner scanner = new Scanner(path);
                    scanner.useDelimiter(",|\\n");
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        SensorReading sensorReading = new SensorReading();
                        sensorReading.setSensorReadingId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                        if (sensorReading.getSensorReadingId() > sensorReadingIdHolder.get()) {
                            sensorReadingIdHolder = new AtomicInteger(Math.toIntExact(sensorReading.getSensorReadingId()));
                        }
                        sensorReading.setSensorId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                        sensorReading.setDateTime(LocalDateTime.parse(scanner.next().replaceAll("\"", "")));
                        sensorReading.setReading(Double.parseDouble(scanner.next().replaceAll("\"", "")));
                        PLOT_HASH_MAP.put(sensorReading.getSensorReadingId(), sensorReading);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void create(final SensorReading sensorReading) throws IOException {
        final int sensorReadingId = sensorReadingIdHolder.incrementAndGet();
        sensorReading.setSensorReadingId((long) sensorReadingId);
        sensorReading.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        PLOT_HASH_MAP.put((long) sensorReadingId, sensorReading);
        CsvManager.writeToFile(sensorReading.receiveHeaders(), sensorReading.toCsv(), "sensorReading");
    }

    @Override
    public List<SensorReading> readAll() {
        return PLOT_HASH_MAP.values().stream().toList();
    }

    @Override
    public SensorReading read(final long id) {
        return PLOT_HASH_MAP.get(id);
    }

    @Override
    public List<SensorReading> readAllSensorsSensorReadings(final long id) {
        return PLOT_HASH_MAP.values().stream().filter(sensorReading -> sensorReading.getSensorId() == id).collect(Collectors.toList());
    }

    @Override
    public boolean update(final SensorReading sensorReading, final long id) throws IOException {
        CsvManager.deleteFromFile(id, "sensorReading");
        sensorReading.setSensorReadingId(id);
        CsvManager.writeToFile(sensorReading.receiveHeaders(), sensorReading.toCsv(), "sensorReading");
        if (PLOT_HASH_MAP.containsKey(id)) {
            PLOT_HASH_MAP.put(id, sensorReading);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(final long id) {
        CsvManager.deleteFromFile(id, "sensorReading");
        return PLOT_HASH_MAP.remove(id) != null;
    }
}
