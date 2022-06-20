package ua.lviv.iot.termPaper.services;

import ua.lviv.iot.termPaper.models.Farmer;

import java.io.IOException;
import java.util.List;

public interface FarmerService {
    /**
     * Створює нового фермера.
     *
     * @param farmer фермер для створення
     */
    void create(Farmer farmer) throws IOException;

    /**
     * Повертає список всіх фермерів.
     *
     * @return список фермерів
     */
    List<Farmer> readAll();

    /**
     * Повертає фермера за його ID.
     *
     * @param id ID фермера
     * @return об'єкт фермера з заданим ID
     */
    Farmer read(long id);

    /**
     * Оновляє фермера з заданим ID.
     *
     * @param farmer фермер, згідно з яким потрібно оновити дані
     * @param id     id фермера якого потрібно оновити
     * @return true якщо дані були оновлені, інакше false
     */
    boolean update(Farmer farmer, long id) throws IOException;

    /**
     * Видаляє фермера з заданим ID.
     *
     * @param id id фермера, якого потрібно видалити
     * @return true якщо фермер був видалений, інакше false
     */
    boolean delete(long id);
}
