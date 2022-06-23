package ua.lviv.iot.termPaper;

import com.opencsv.CSVWriter;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        Path path = Path.of("csvFiles/user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
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
        Path path = Path.of("csvFiles/user-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv");
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
        CsvManager.init("farmer");
        String path = "csvFiles/farmer-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = FarmerServiceImpl.farmerIdHolder.get() + 1;
        if(Files.exists(Path.of(path))){
            FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true);
            writer.write("\""+id+"\",\"Surname Name\"");
            writer.close();
        }
        else{
            File file = new File(path);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("\"farmerId\",\"fullName\"\n");
            writer.write("\""+id+"\",\"Surname Name\"");
            writer.close();
        }
        CsvManager.init("farmer");

        SoftAssertions softAssertions = new SoftAssertions();
        FarmerServiceImpl farmerService = new FarmerServiceImpl();
        Farmer farmer = new Farmer();
        farmer.setFarmerId(id);
        farmer.setFullName("Surname Name");
        softAssertions.assertThat(farmerService.read(id).equals(farmer)).isTrue();
        CsvManager.deleteFromFile(id, "farmer");
        softAssertions.assertAll();
    }

    @Test
    void testReadPlotFromCsv() throws IOException {
        CsvManager.init("plot");

        String path = "csvFiles/plot-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = PlotServiceImpl.plotIdHolder.get() + 1;
        if(Files.exists(Path.of(path))){
            FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true);
            writer.write("\""+id+"\",\"1\",\"300.0\",\"Location\"");
            writer.close();
        }
        else{
            File file = new File(path);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("\"plotId\",\"farmerId\",\"area\",\"location\"\n");
            writer.write("\""+id+"\",\"1\",\"300.0\",\"Location\"");
            writer.close();
        }
        CsvManager.init("plot");


        SoftAssertions softAssertions = new SoftAssertions();
        PlotServiceImpl plotService = new PlotServiceImpl();
        Plot plot = new Plot();
        plot.setPlotId(id);
        plot.setArea(300);
        plot.setLocation("Location");
        plot.setFarmerId(1L);
        softAssertions.assertThat(plotService.read(id).equals(plot)).isTrue();
        CsvManager.deleteFromFile(id, "plot");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorFromCsv() throws IOException {
        CsvManager.init("sensor");

        String path = "csvFiles/sensor-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = SensorServiceImpl.sensorIdHolder.get() + 1;
        if(Files.exists(Path.of(path))){
            FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true);
            writer.write("\""+id+"\",\"1\",\"Location\",\"LIGHT\"");
            writer.close();
        }
        else{
            File file = new File(path);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("\"sensorId\",\"plotId\",\"location\",\"typeOfSensor\"\n");
            writer.write("\""+id+"\",\"1\",\"Location\",\"LIGHT\"");
            writer.close();
        }
        CsvManager.init("sensor");

        SoftAssertions softAssertions = new SoftAssertions();
        SensorServiceImpl sensorService = new SensorServiceImpl();
        Sensor sensor = new Sensor();
        sensor.setSensorId(id);
        sensor.setPlotId(1L);
        sensor.setTypeOfSensor(SensorType.LIGHT);
        sensor.setLocation("Location");
        softAssertions.assertThat(sensorService.read(id).equals(sensor)).isTrue();
        CsvManager.deleteFromFile(id, "sensor");
        softAssertions.assertAll();
    }

    @Test
    void testReadSensorReadingFromCsv() throws IOException {
        CsvManager.init("sensorReading");
        String path = "csvFiles/sensorReading-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        long id = SensorReadingServiceImpl.sensorReadingIdHolder.get() + 1;
        LocalDateTime localDateTime = LocalDateTime.now();
        if(Files.exists(Path.of(path))){
            FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8, true);
            writer.write("\""+id+"\",\"1\",\""+localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"\",\"90.0\"");
            writer.close();
        }
        else{
            File file = new File(path);
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            writer.write("\"sensorReadingId\",\"sensorId\",\"dateTime\",\"reading\"\n");
            writer.write("\""+id+"\",\"1\",\"2022-06-16T14:35:09\",\"LIGHT\"");
            writer.close();
        }
        CsvManager.init("sensorReading");

        SoftAssertions softAssertions = new SoftAssertions();
        SensorReadingServiceImpl sensorReadingService = new SensorReadingServiceImpl();
        SensorReading sensorReading = new SensorReading();
        sensorReading.setSensorReadingId(id);
        sensorReading.setReading(90);
        sensorReading.setSensorId(1L);
        sensorReading.setDateTime(localDateTime);
        softAssertions.assertThat(sensorReadingService.read(id).equals(sensorReading)).isTrue();
        CsvManager.deleteFromFile(id, "sensorReading");
        softAssertions.assertAll();
    }

}
