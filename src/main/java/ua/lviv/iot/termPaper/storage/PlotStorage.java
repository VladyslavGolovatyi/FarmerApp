package ua.lviv.iot.termPaper.storage;

import org.springframework.stereotype.Component;
import ua.lviv.iot.termPaper.models.Plot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class PlotStorage extends AbstractStorage {

    private final Map<Long, Plot> plots = new HashMap<>();

    private Long nextAvailableId = 1L;

    @Override
    public Long getNextAvailableId() {
        return nextAvailableId;
    }

    public void addPlotToHashMap(final Plot plot) {
        plot.setPlotId(nextAvailableId++);
        plots.put(plot.getPlotId(), plot);
    }

    public List<Plot> getAllPlots() {
        return new ArrayList<>(plots.values());
    }

    public List<Plot> getAllFarmersPlots(final long farmerId) {
        return plots.values().stream().filter(plot -> plot.getFarmerId() == farmerId).collect(Collectors.toList());
    }

    public Plot getPlotById(final Long id) {
        return plots.get(id);
    }

    public boolean deleteFromHashMap(final Long id) {
        return plots.remove(id) != null;
    }

    public boolean updateHashMap(long id, final Plot plot) {
        if (!plots.containsKey(id)) {
            return false;
        } else {
            plots.put(id, plot);
        }
        return true;
    }

    @Override
    protected void fromCsvToRecord(final String[] values) {
        Plot plot = new Plot();
        plot.setPlotId(Long.valueOf(values[0].replaceAll("\"", "")));
        if (plot.getPlotId() > nextAvailableId) {
            nextAvailableId = plot.getPlotId() + 1;
        }
        plot.setFarmerId(Long.valueOf(values[1].replaceAll("\"", "")));
        plot.setArea(Double.parseDouble(values[2].replaceAll("\"", "")));
        plot.setLocation(values[3].replaceAll("\"", "").replaceAll("\r", ""));
        plots.put(plot.getPlotId(), plot);
    }

}
