package ghart.space.server;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

/**
 * Adds a wrapper around the {@link InfluxDBClientFactory} class containing details to create a connection.
 * Our application will never use more than one org, bucket, url, or API token. 
 * 
 * To access the influx data base call {@link #create()} to get an {@link InfluxDBClient} object.
 */
public class InfluxDBConnectionFactory {
    
    private static String token = System.getenv("INFLUX_TOKEN");
    
    public static final String URL = "http://bomato.ghart.space:8086";
    public static final String CAR_BUCKET = "car-test";
    public static final String ORG = "ghartorg";

    public InfluxDBConnectionFactory(){}


    /**
     * Create an {@link InfluxDBClient} instance and return it. Abstracts db parameters.
     * 
     * @return {@link InfluxDBClient} instance
     */
    public static InfluxDBClient create(){
        return InfluxDBClientFactory.create(URL, token.toCharArray());
    }

}
