package ua.lviv.iot.termPaper.services;

import org.springframework.stereotype.Service;
import ua.lviv.iot.termPaper.models.Sensor;

import java.io.IOException;
import java.util.List;

@Service
public interface SensorService {
    /**
     * Створює новий сенсор.
     *
     * @param sensor сенсор для створення
     */
    void create(Sensor sensor) throws IOException;

    /**
     * Повертає список всіх сенсорів.
     * @return список сенсорів
     */
    List<Sensor> readAll();

    /**
     * Повертає сенсор з певним id.
     * @param id   id сенсора
     * @return сенсор
     */
    Sensor read(long id);

    /**
     * Повертає список всіх сенсорів ділянки.
     * @param id   id ділянки
     * @return список сенсорів
     */
    List<Sensor> readAllPlotsSensors(long id);

    /**
     * Оновляє сенсор з заданим ID.
     *
     * @param sensor сенсор, згідно з яким потрібно оновити дані
     * @param id   id сенсора який потрібно оновити
     * @return true якщо дані були оновлені, інакше false
     */
    boolean update(Sensor sensor, long id) throws IOException;

    /**
     * Видаляє сенсор з заданим ID.
     *
     * @param id id сенсора, який потрібно видалити
     * @return true якщо сенсор був видалений, інакше false
     */
    boolean delete(long id);
}
