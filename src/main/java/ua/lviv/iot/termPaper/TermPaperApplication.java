package ua.lviv.iot.termPaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.lviv.iot.termPaper.servicesImpl.FarmerServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.PlotServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorReadingServiceImpl;
import ua.lviv.iot.termPaper.servicesImpl.SensorServiceImpl;

@SpringBootApplication
public class TermPaperApplication {

    public static void main(String[] args) {
        SpringApplication.run(TermPaperApplication.class, args);
        FarmerServiceImpl.init();
        PlotServiceImpl.init();
        SensorServiceImpl.init();
        SensorReadingServiceImpl.init();
    }

}
