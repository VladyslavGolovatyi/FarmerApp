package ua.lviv.iot.termPaper.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.models.Farmer;
import ua.lviv.iot.termPaper.services.FarmerService;
import ua.lviv.iot.termPaper.storage.FarmerStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public final class FarmerServiceImpl implements FarmerService {

    @Autowired
    private FarmerStorage farmerStorage;

    @Override
    public void create(final Farmer farmer) throws IOException {
        farmerStorage.addFarmerToHashMap(farmer);
        farmerStorage.writeToFile(farmer.receiveHeaders(), farmer.toCsv(), "farmer");
    }

    @Override
    public List<Farmer> readAll() {
        return farmerStorage.getAllFarmers();
    }

    @Override
    public Farmer read(final long id) {
        return farmerStorage.getFarmerById(id);
    }

    @Override
    public boolean update(final Farmer farmer, final long id) throws IOException {
        farmerStorage.deleteFromFile(id, "farmer");
        farmer.setFarmerId(id);
        farmerStorage.writeToFile(farmer.receiveHeaders(), farmer.toCsv(), "farmer");
        return farmerStorage.updateHashMap(id, farmer);
    }

    @Override
    public boolean delete(final long id) {
        farmerStorage.deleteFromFile(id, "farmer");
        return farmerStorage.deleteFromHashMap(id);
    }

    @PostConstruct
    private void loadFarmers() {
        farmerStorage.load("farmer");
    }

}
