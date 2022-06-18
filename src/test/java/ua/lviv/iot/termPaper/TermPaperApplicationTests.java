package ua.lviv.iot.termPaper;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ua.lviv.iot.termPaper.managers.CsvManager;
import ua.lviv.iot.termPaper.models.*;
import ua.lviv.iot.termPaper.servicesImpl.FarmerServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.PlotServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorReadingServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class TermPaperApplicationTests {

    @Test
    void testWriteToCsvOneElementCase() throws IOException {
        Path path = Path.of("user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        if (Files.exists(path))
            Files.delete(path);
        CsvManager.writeToFile(new String[]{"name", "surname"}, new String[]{"David", "Johns"}, "user");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals("""
                "name","surname"
                "David","Johns"
                """, Files.readString(path));
    }

    @Test
    void testWriteToCsvRegularCase() throws IOException {
        Path path = Path.of("user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        if (Files.exists(path))
            Files.delete(path);
        CsvManager.writeToFile(new String[]{"name", "surname"}, new String[]{"David", "Johns"}, "user");
        CsvManager.writeToFile(new String[]{"name", "surname"}, new String[]{"Vasyl", "Yarmolenko"}, "user");
        Assertions.assertTrue(Files.exists(path));
        Assertions.assertEquals("""
                "name","surname"
                "David","Johns"
                "Vasyl","Yarmolenko"
                """, Files.readString(path));
    }

    @Test
    void testReadFarmerFromCsv() throws IOException {
        FarmerServiceImpl.init();
        Path path = Path.of("farmer-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        Farmer farmer = new Farmer();
        final long id = 77L;
        farmer.setFarmerId(id);
        farmer.setFullName("Petrov Ivan");
        CsvManager.writeToFile(farmer.receiveHeaders(), farmer.toCsv(), "farmer");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(Files.exists(path)).isTrue();
        softAssertions.assertThat(Files.readString(path).contains("""
                "77","Petrov Ivan"
                """)).isTrue();
        CsvManager.deleteFromFile(id, "farmer");
        softAssertions.assertAll();
    }

    @Test
    void testReadPlotFromCsv() throws IOException {
        PlotServiceImpl.init();
        Path path = Path.of("plot-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        Plot plot = new Plot();
        final long id = 50L;
        final long id1 = 60L;
        final double area = 500;
        plot.setPlotId(id);
        plot.setFarmerId(id1);
        plot.setArea(area);
        plot.setLocation("Lviv");
        CsvManager.writeToFile(plot.receiveHeaders(), plot.toCsv(), "plot");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(Files.exists(path)).isTrue();
        softAssertions.assertThat(Files.readString(path).contains("""
                "50","60","500.0","Lviv"
                """)).isTrue();
        CsvManager.deleteFromFile(id, "plot");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorFromCsv() throws IOException {
        SensorServiceImpl.init();
        Path path = Path.of("sensor-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        Sensor sensor = new Sensor();
        final long id = 77L;
        sensor.setSensorId(id);
        sensor.setPlotId(id);
        sensor.setLocation("Kyiv");
        sensor.setTypeOfSensor(SensorType.LIGHT);
        CsvManager.writeToFile(sensor.receiveHeaders(), sensor.toCsv(), "sensor");

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(Files.exists(path)).isTrue();
        softAssertions.assertThat(Files.readString(path).contains("""
                "77","77","Kyiv","LIGHT"
                """)).isTrue();
        CsvManager.deleteFromFile(id, "sensor");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorReadingFromCsv() throws IOException {
        SensorReadingServiceImpl.init();
        Path path = Path.of("sensorReading-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
        SensorReading sensorReading = new SensorReading();
        final long id = 7L;
        final long id1 = 4L;
        final double reading = 120;
        final LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        sensorReading.setSensorReadingId(id);
        sensorReading.setSensorId(id1);
        sensorReading.setDateTime(localDateTime);
        sensorReading.setReading(reading);
        CsvManager.writeToFile(sensorReading.receiveHeaders(), sensorReading.toCsv(), "sensorReading");

        System.out.println(localDateTime);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(Files.exists(path)).isTrue();
        softAssertions.assertThat(Files.readString(path).contains("\"7\",\"4\",\"" + localDateTime + "\",\"120.0\"")).isTrue();
        CsvManager.deleteFromFile(id, "sensorReading");
        softAssertions.assertAll();
    }

}
