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
    public static final Map<Long, Farmer> FARMER_HASH_MAP = new HashMap<>();

    // Змінна для генерації ID фермера
    public static AtomicInteger farmerIdHolder = new AtomicInteger();

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
