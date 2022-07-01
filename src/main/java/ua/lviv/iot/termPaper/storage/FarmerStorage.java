package ua.lviv.iot.termPaper.storage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.termPaper.models.Farmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public final class FarmerStorage extends AbstractStorage {

    private final Map<Long, Farmer> farmers = new HashMap<>();

    private Long nextAvailableId = 1L;

    public Long getNextAvailableId() {
        return nextAvailableId;
    }

    public void addFarmerToHashMap(final Farmer farmer) {
        farmer.setFarmerId(nextAvailableId++);
        farmers.put(farmer.getFarmerId(), farmer);
    }

    public List<Farmer> getAllFarmers() {
        return new ArrayList<>(farmers.values());
    }

    public Farmer getFarmerById(final Long id) {
        return farmers.get(id);
    }

    public boolean deleteFromHashMap(final Long id) {
        return farmers.remove(id) != null;
    }

    public boolean updateHashMap(final long id, final Farmer farmer) {
        if (!farmers.containsKey(id)) {
            return false;
        } else {
            farmers.put(id, farmer);
        }
        return true;
    }

    @Override
    protected void fromCsvToRecord(final String[] values) {
        Farmer farmer = new Farmer();
        farmer.setFarmerId(Long.valueOf(values[0].replaceAll("\"", "")));
        if (farmer.getFarmerId() > nextAvailableId) {
            nextAvailableId = farmer.getFarmerId() + 1;
        }
        farmer.setFullName(values[1].replaceAll("\"", "").replaceAll("\r", ""));
        farmers.put(farmer.getFarmerId(), farmer);
    }

}
