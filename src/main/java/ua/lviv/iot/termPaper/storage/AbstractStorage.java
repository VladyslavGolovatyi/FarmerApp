package ua.lviv.iot.termPaper.storage;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public abstract class AbstractStorage {

    protected abstract void fromCsvToRecord(String[] values);

    public abstract Long getNextAvailableId();

    public void load(final String type) {
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); ++i) {
            Path path = getFileName(type, i);
            if (Files.exists(path)) {
                try (Scanner scanner = new Scanner(path)) {
                    scanner.useDelimiter(",|\\n");
                    scanner.nextLine();
                    while (scanner.hasNext()) {
                        fromCsvToRecord(scanner.nextLine().split(","));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Path getFileName(final String type, final int date) {
        return Path.of("csvFiles/" + type + "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu-MM-")) + date + ".csv");
    }

    public void writeToFile(final String[] headers, final String[] stringRecordRepresentation, final String type) throws IOException {
        Path path = getFileName(type, LocalDate.now().getDayOfMonth());
        writeLineToFile(headers, stringRecordRepresentation, Files.exists(path), path);
    }

    private void writeLineToFile(final String[] headers, final String[] stringRecordRepresentation,
                                 final boolean skipHeaders, final Path path) throws IOException {
        try (FileWriter outputFile = new FileWriter(path.toFile(), StandardCharsets.UTF_8, skipHeaders);
             CSVWriter writer = new CSVWriter(outputFile)) {
            if (!skipHeaders) {
                writer.writeNext(headers);
            }
            writer.writeNext(stringRecordRepresentation);
        }
    }

    public void deleteFromFile(final long id, final String type) {
        for (int i = 1; i <= LocalDate.now().getDayOfMonth(); i++) {
            Path path = getFileName(type, i);
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
