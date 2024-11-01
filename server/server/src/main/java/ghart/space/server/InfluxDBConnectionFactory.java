package ghart.space.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

/**
 * Adds a wrapper around the {@link InfluxDBClientFactory} class containing details to create a connection.
 * Our application will never use more than one org, bucket, url, or API token. 
 * 
 * To access the influx data base call {@link #create()} to get an {@link InfluxDBClient} object.
 */
public class InfluxDBConnectionFactory {
    // prioritize environment variables for configuration
    // if environment variables do not exist then fall back to file paths for secrets

    private static String token = System.getenv("INFLUXDB_TOKEN");
    private static String tokenFilePath = System.getenv("INFLUXDB_TOKEN_FILE");
    
    
    
    // public static final String URL = "http://bomato.ghart.space:8086";
    public static final String URL = System.getenv("INFLUXDB_URL");
    public static final String CAR_BUCKET = System.getenv("INFLUXDB_BUCKET");
    public static final String ORG = System.getenv("INFLUXDB_ORG"); 
    public static final String MEASUREMENT = System.getenv("INFLUXDB_MEASUREMENT"); 
    
    public InfluxDBConnectionFactory(){}
    
    public static void initiateFactory(){
        // TODO there is a better way to do this using springboots spring boot configuration parameters

        if( token == null){
            if(tokenFilePath == null){
                System.err.println("Neither INFLUXDB_TOKEN or INFLUXDB_TOKEN_FILE environment variables " + 
                                    "are configured. Application exiting.");
                System.exit(0);
            }
            System.out.println("Envrironment variable INFLUXDB_TOKEN not found. Searching for file: " + tokenFilePath);
            try{
                token = Files.readString(Path.of(tokenFilePath));
            }
            catch(IOException e){
                System.err.println("Could not find or read file " + tokenFilePath);
                System.err.println("Application exiting.");
                System.exit(0);
            }
        }

        if(URL == null){
            System.err.println("Could not find environment variable INFLUXDB_URL. Application exit.");
            System.exit(0);
        }
        if(CAR_BUCKET == null){
            System.err.println("Could not find environment variable INFLUXDB_BUCKET. Application exit");
            System.exit(0);
        }
        if(ORG == null){
            System.err.println("Could not find environment variable INFLUXDB_ORG. Application exit.");
            System.exit(0);
        }
        if(MEASUREMENT == null){
            System.err.println("Could not find environment variable INFLUXDB_MEASUREMENT. Application exit.");
            System.exit(0);
        }
    }

    /**
     * Create an {@link InfluxDBClient} instance and return it. Abstracts db parameters.
     * 
     * @return {@link InfluxDBClient} instance
     */
    public static InfluxDBClient create(){
        // TODO add check on if token is null. causes a crash
        return InfluxDBClientFactory.create(URL, token.toCharArray());
    }

}
