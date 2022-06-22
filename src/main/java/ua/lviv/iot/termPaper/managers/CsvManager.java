package ua.lviv.iot.termPaper.managers;

import com.opencsv.CSVWriter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class CsvManager {

    private CsvManager() {
    }

    public static void init(String type) {
        final int maxNumberOfDaysInMonth = 31;
        for (int i = 1; i <= maxNumberOfDaysInMonth; ++i) {
            Path path = Path.of("csvFiles/"+type+"-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + i + ".csv");
            if (Files.exists(path)) {
                try (Scanner scanner = new Scanner(path)) {
                    scanner.useDelimiter(",|\\n");
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        switch (type) {
                            case "farmer" -> {
                                Farmer farmer = new Farmer();
                                farmer.setFarmerId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                if (farmer.getFarmerId() > FarmerServiceImpl.farmerIdHolder.get()) {
                                    FarmerServiceImpl.farmerIdHolder = new AtomicInteger(Math.toIntExact(farmer.getFarmerId()));
                                }
                                farmer.setFullName(scanner.next().replaceAll("\"", "").replaceAll("\r", ""));
                                FarmerServiceImpl.FARMER_HASH_MAP.put(farmer.getFarmerId(), farmer);
                            }
                            case "plot" -> {
                                Plot plot = new Plot();
                                plot.setPlotId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                if (plot.getPlotId() > PlotServiceImpl.plotIdHolder.get()) {
                                    PlotServiceImpl.plotIdHolder = new AtomicInteger(Math.toIntExact(plot.getPlotId()));
                                }
                                plot.setFarmerId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                plot.setArea(Double.parseDouble(scanner.next().replaceAll("\"", "")));
                                plot.setLocation(scanner.next().replaceAll("\"", "").replaceAll("\r", ""));
                                PlotServiceImpl.PLOT_HASH_MAP.put(plot.getPlotId(), plot);
                            }
                            case "sensor" -> {
                                Sensor sensor = new Sensor();
                                sensor.setSensorId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                if (sensor.getSensorId() > SensorServiceImpl.sensorIdHolder.get()) {
                                    SensorServiceImpl.sensorIdHolder = new AtomicInteger(Math.toIntExact(sensor.getSensorId()));
                                }
                                sensor.setPlotId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                sensor.setLocation(scanner.next().replaceAll("\"", "").replaceAll("\r", ""));
                                sensor.setTypeOfSensor(SensorType.valueOf(scanner.next().replaceAll("\"", "").trim()));
                                SensorServiceImpl.SENSOR_HASH_MAP.put(sensor.getSensorId(), sensor);
                            }
                            case "sensorReading" -> {
                                SensorReading sensorReading = new SensorReading();
                                sensorReading.setSensorReadingId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                if (sensorReading.getSensorReadingId() > SensorReadingServiceImpl.sensorReadingIdHolder.get()) {
                                    SensorReadingServiceImpl.sensorReadingIdHolder = new AtomicInteger(Math.toIntExact(sensorReading.getSensorReadingId()));
                                }
                                sensorReading.setSensorId(Long.valueOf(scanner.next().replaceAll("\"", "")));
                                sensorReading.setDateTime(LocalDateTime.parse(scanner.next().replaceAll("\"", "")));
                                sensorReading.setReading(Double.parseDouble(scanner.next().replaceAll("\"", "")));
                                SensorReadingServiceImpl.SENSOR_READING_HASH_MAP.put(sensorReading.getSensorReadingId(), sensorReading);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void writeToFile(final String[] receiveHeaders, final String[] toCsv, final String type) throws IOException {
        String path = "csvFiles/"+type + "-" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        if (Files.exists(Path.of(path))) {
            FileWriter outputFile = new FileWriter(path, StandardCharsets.UTF_8, true);
            CSVWriter writer = new CSVWriter(outputFile);
            writer.writeNext(toCsv);
            writer.close();
        } else {
            File file = new File(path);
            FileWriter outputFile = new FileWriter(file, StandardCharsets.UTF_8);
            CSVWriter writer = new CSVWriter(outputFile);
            writer.writeNext(receiveHeaders);
            writer.writeNext(toCsv);
            writer.close();
        }
    }

    public static void deleteFromFile(final long id, final String type) {
        final int maxNumberOfDaysInMonth = 31;
        for (int i = 1; i < maxNumberOfDaysInMonth; i++) {
            Path path = Path.of("csvFiles/"+type + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + i + ".csv");
            if (Files.exists(path)) {
                try {
                    List<String> out = Files.lines(path)
                            .filter(line -> !line.startsWith("\"" + id + "\""))
                            .collect(Collectors.toList());
                    Files.write(path, out);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
