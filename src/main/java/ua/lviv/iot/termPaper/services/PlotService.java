package ua.lviv.iot.termPaper.services;

import ua.lviv.iot.termPaper.models.Plot;

import java.io.IOException;
import java.util.List;

public interface PlotService {
    /**
     * Створює нову ділянку.
     *
     * @param plot ділянка для створення
     */
    void create(Plot plot) throws IOException;

    List<Plot> readAll();

    Plot read(long id);

    /**
     * Повертає список всіх ділянок фермера.
     *
     * @param id id діялнки яку потрібно оновити
     * @return список ділянок
     */
    List<Plot> readAllFarmersPlots(long id);

    /**
     * Оновляє ділянку з заданим ID.
     *
     * @param plot ділянка, згідно з якою потрібно оновити дані
     * @param id   id діялнки яку потрібно оновити
     * @return true якщо дані були оновлені, інакше false
     */
    boolean update(Plot plot, long id) throws IOException;

    /**
     * Видаляє ділянку з заданим ID.
     *
     * @param id id ділянки, яку потрібно видалити
     * @return true якщо ділянка була видалена, інакше false
     */
    boolean delete(long id);
}
