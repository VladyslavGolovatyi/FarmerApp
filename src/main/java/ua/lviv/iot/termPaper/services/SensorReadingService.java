package ua.lviv.iot.termPaper.services;

import ua.lviv.iot.termPaper.models.SensorReading;

import java.io.IOException;
import java.util.List;

public interface SensorReadingService {

    /**
     * Створює новий показ.
     *
     * @param sensorReading сенсор для створення
     */
    void create(SensorReading sensorReading) throws IOException;

    /**
     * Повертає список всіх показів.
     *
     * @return список показів
     */
    List<SensorReading> readAll();

    /**
     * Повертає показ з певним id.
     *
     * @param id id показу
     * @return показ
     */
    SensorReading read(long id);

    /**
     * Повертає список всіх показів сенсора.
     *
     * @param id id сенсора
     * @return список показів
     */
    List<SensorReading> readAllSensorsSensorReadings(long id);

    /**
     * Оновляє показ з заданим ID.
     *
     * @param sensorReading показ, згідно з яким потрібно оновити дані
     * @param id            id показу який потрібно оновити
     * @return true якщо дані були оновлені, інакше false
     */
    boolean update(SensorReading sensorReading, long id) throws IOException;

    /**
     * Видаляє показ з заданим ID.
     *
     * @param id id показу, який потрібно видалити
     * @return true якщо показ був видалений, інакше false
     */
    boolean delete(long id);
}
