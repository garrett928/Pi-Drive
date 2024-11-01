package ghart.space.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelemetryApplication {

    public static void main(String... args) {

        System.out.println("Hello from Pi-Drive");
        InfluxDBConnectionFactory.initiateFactory();
        SpringApplication.run(TelemetryApplication.class, args);
    }
}
