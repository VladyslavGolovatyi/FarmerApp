package ua.lviv.iot.termPaper;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.termPaper.models.*;
import ua.lviv.iot.termPaper.servicesImpl.FarmerServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.PlotServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorReadingServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorServiceImpl;
import ua.lviv.iot.termPaper.storage.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class TestTermPaperApplication {

    @Autowired
    FarmerServiceImpl farmerService;
    @Autowired
    FarmerStorage farmerStorage;
    @Autowired
    PlotStorage plotStorage;
    @Autowired
    SensorStorage sensorStorage;
    @Autowired
    SensorReadingStorage sensorReadingStorage;
    @Autowired
    PlotServiceImpl plotService;
    @Autowired
    SensorServiceImpl sensorService;
    @Autowired
    SensorReadingServiceImpl sensorReadingService;

    AbstractStorage abstractStorage = new AbstractStorage() {
        @Override
        protected void fromCsvToRecord(String[] values) {

        }

        @Override
        public Long getNextAvailableId() {
            return null;
        }
    };

    @Test
    void testWriteToCsvOneElementCase() throws IOException {
        Path path = Path.of("csvFiles/user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        if (Files.exists(path))
            Files.delete(path);
        abstractStorage.writeToFile(new String[]{"name", "surname"}, new String[]{"David", "Johns"}, "user");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals("""
                "name","surname"
                "David","Johns"
                """, Files.readString(path));
    }

    @Test
    void testWriteToCsvRegularCase() throws IOException {
        Path path = Path.of("csvFiles/user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        if (Files.exists(path))
            Files.delete(path);
        abstractStorage.writeToFile(new String[]{"name", "surname"}, new String[]{"David", "Johns"}, "user");
        abstractStorage.writeToFile(new String[]{"name", "surname"}, new String[]{"Vasyl", "Yarmolenko"}, "user");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals("""
                "name","surname"
                "David","Johns"
                "Vasyl","Yarmolenko"
                """, Files.readString(path));
    }

    @Test
    void testReadFarmerFromCsv() throws IOException {
        farmerStorage.load("farmer");
        String path = "csvFiles/farmer-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = farmerStorage.getNextAvailableId();
        if (Files.exists(Path.of(path))) {
            try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                writer.write("\"" + id + "\",\"Surname Name\"");
            }
        } else {
            File file = new File(path);
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("\"farmerId\",\"fullName\"\n");
                writer.write("\"" + id + "\",\"Surname Name\"");
            }
        }
        farmerStorage.load("farmer");

        SoftAssertions softAssertions = new SoftAssertions();
        Farmer farmer = new Farmer();
        farmer.setFarmerId(id);
        farmer.setFullName("Surname Name");
        softAssertions.assertThat(farmerService.read(id).equals(farmer)).isTrue();
        farmerStorage.deleteFromFile(id, "farmer");
        softAssertions.assertAll();
    }

    @Test
    void testReadPlotFromCsv() throws IOException {
        plotStorage.load("plot");

        String path = "csvFiles/plot-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = plotStorage.getNextAvailableId();
        if (Files.exists(Path.of(path))) {
            try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                writer.write("\"" + id + "\",\"1\",\"300.0\",\"Location\"");
            }
        } else {
            File file = new File(path);
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("\"plotId\",\"farmerId\",\"area\",\"location\"\n");
                writer.write("\"" + id + "\",\"1\",\"300.0\",\"Location\"");
            }
        }
        plotStorage.load("plot");


        SoftAssertions softAssertions = new SoftAssertions();
        Plot plot = new Plot();
        plot.setPlotId(id);
        plot.setArea(300);
        plot.setLocation("Location");
        plot.setFarmerId(1L);
        softAssertions.assertThat(plotService.read(id).equals(plot)).isTrue();
        plotStorage.deleteFromFile(id, "plot");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorFromCsv() throws IOException {
        sensorStorage.load("sensor");

        String path = "csvFiles/sensor-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = sensorStorage.getNextAvailableId();
        if (Files.exists(Path.of(path))) {
            try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                writer.write("\"" + id + "\",\"1\",\"Location\",\"LIGHT\"");
            }
        } else {
            File file = new File(path);
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("\"sensorId\",\"plotId\",\"location\",\"typeOfSensor\"\n");
                writer.write("\"" + id + "\",\"1\",\"Location\",\"LIGHT\"");
            }
        }
        sensorStorage.load("sensor");

        SoftAssertions softAssertions = new SoftAssertions();
        Sensor sensor = new Sensor();
        sensor.setSensorId(id);
        sensor.setPlotId(1L);
        sensor.setTypeOfSensor(SensorType.LIGHT);
        sensor.setLocation("Location");
        softAssertions.assertThat(sensorService.read(id).equals(sensor)).isTrue();
        sensorStorage.deleteFromFile(id, "sensor");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorReadingFromCsv() throws IOException {
        sensorReadingStorage.load("sensorReading");
        String path = "csvFiles/sensorReading-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = sensorReadingStorage.getNextAvailableId();
        LocalDateTime localDateTime = LocalDateTime.now();
        if (Files.exists(Path.of(path))) {
            try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true)) {
                writer.write("\"" + id + "\",\"1\",\"" + localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\",\"90.0\"");
            }
        } else {
            File file = new File(path);
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write("\"sensorReadingId\",\"sensorId\",\"dateTime\",\"reading\"\n");
                writer.write("\"" + id + "\",\"1\",\"2022-06-16T14:35:09\",\"LIGHT\"");
            }
        }
        sensorReadingStorage.load("sensorReading");

        SoftAssertions softAssertions = new SoftAssertions();
        SensorReading sensorReading = new SensorReading();
        sensorReading.setSensorReadingId(id);
        sensorReading.setReading(90);
        sensorReading.setSensorId(1L);
        sensorReading.setDateTime(localDateTime);
        softAssertions.assertThat(sensorReadingService.read(id).equals(sensorReading)).isTrue();
        sensorReadingStorage.deleteFromFile(id, "sensorReading");
        softAssertions.assertAll();
    }

}
