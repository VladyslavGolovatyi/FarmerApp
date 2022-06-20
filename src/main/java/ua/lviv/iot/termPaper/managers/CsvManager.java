package ua.lviv.iot.termPaper.managers;

import com.opencsv.CSVWriter;
import ua.lviv.iot.termPaper.models.Farmer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class CsvManager {

    private CsvManager() {
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
