package ghart.space.server;

import ghart.space.server.car.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CarRepository CarRepository) {
    
        return args -> {
            CarRepository.save(new Car("Honda", "Fit", "2007"));
            CarRepository.save(new Car("Jeep", "Renegade", "2018"));
    
            CarRepository.findAll().forEach(car -> log.info("Preloaded " + car));            
        };
    }
}
