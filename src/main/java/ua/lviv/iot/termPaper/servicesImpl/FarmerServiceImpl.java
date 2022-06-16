package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.managers.CsvManager;
import ua.lviv.iot.termPaper.models.Farmer;
import ua.lviv.iot.termPaper.services.FarmerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public final class FarmerServiceImpl implements FarmerService {

    // Сховище фермерів
    private static final Map<Long, Farmer> FARMER_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID фермера
    private static AtomicInteger farmerIdHolder = new AtomicInteger();

    public static void init() {
        final int maxNumberOfDaysInMonth = 31;
        for (int i = 1; i <= maxNumberOfDaysInMonth; ++i) {
            Path path = Path.of("farmer-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + i + ".csv");
            if (Files.exists(path)) {
                try {
                    Scanner scanner = new Scanner(path);
                    scanner.useDelimiter(",|\\n");
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        Farmer farmer = new Farmer();
                        farmer.setFarmerId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                        if (farmer.getFarmerId() > farmerIdHolder.get()) {
                            farmerIdHolder = new AtomicInteger(Math.toIntExact(farmer.getFarmerId()));
                        }
                        farmer.setFullName(scanner.next().replaceAll("\"", ""));
                        FARMER_HASH_MAP.put(farmer.getFarmerId(), farmer);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void create(final Farmer farmer) throws IOException {
        final int farmerId = farmerIdHolder.incrementAndGet();
        farmer.setFarmerId((long) farmerId);
        FARMER_HASH_MAP.put(farmer.getFarmerId(), farmer);
        CsvManager.writeToFile(farmer.receiveHeaders(), farmer.toCsv(), "farmer");

    }

    @Override
    public List<Farmer> readAll() {
        return new ArrayList<>(FARMER_HASH_MAP.values());
    }

    @Override
    public Farmer read(final long id) {
        return FARMER_HASH_MAP.get(id);
    }

    @Override
    public boolean update(final Farmer farmer, final long id) throws IOException {
        CsvManager.deleteFromFile(id, "farmer");
        farmer.setFarmerId(id);
        CsvManager.writeToFile(farmer.receiveHeaders(), farmer.toCsv(), "farmer");
        if (FARMER_HASH_MAP.containsKey(id)) {
            FARMER_HASH_MAP.put(id, farmer);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(final long id) {
        CsvManager.deleteFromFile(id, "farmer");
        return FARMER_HASH_MAP.remove(id) != null;
    }
}
