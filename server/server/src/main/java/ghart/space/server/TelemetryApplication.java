package ghart.space.server;

import java.time.Instant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

@SpringBootApplication
public class TelemetryApplication {

    public static void main(String... args) {

        InfluxDBClient client = InfluxDBConnectionFactory.create();

        Point point = Point
        .measurement("mem")
        .addTag("host", "host1")
        .addField("used_percent", 20)
        .time(Instant.now(), WritePrecision.NS);    

        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writePoint(InfluxDBConnectionFactory.CAR_BUCKET, InfluxDBConnectionFactory.ORG, point);

        SpringApplication.run(TelemetryApplication.class, args);
    }
}
