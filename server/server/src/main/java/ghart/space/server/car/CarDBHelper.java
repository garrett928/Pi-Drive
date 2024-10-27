package ghart.space.server.car;

import java.util.List;
import java.time.Instant;
import java.util.ArrayList;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import ghart.space.server.InfluxDBConnectionFactory;

public class CarDBHelper {

    public CarDBHelper(){}

    /**
     * Find all cars in the InfluxDB bucket and return them as a list of {@link Car}
     * 
     * @return {@link List} of {@link Car} entrys found in the DB
     */
    public List<Car> findAllCars(){
        InfluxDBClient client = this.getClient();
        List<Car> carList = new ArrayList<Car>();

        // grouping by measurement puts all results in the same fluxtable which matches the data representation we expect
        String query = """
                        from(bucket: \"%s\")
                        |> range(start: -1y)
                        |> group(columns: [\"_measurement\"])
                        |> filter(fn: (r) => r[\"_measurement\"] == \"carTelemetry\")
                        |> keep(columns: [\"id\", \"make\", \"model\", \"year\"])
                        |> unique(column: \"id\")
                        """.formatted(InfluxDBConnectionFactory.CAR_BUCKET);

        FluxTable fluxTable = client.getQueryApi().query(query, InfluxDBConnectionFactory.ORG).get(0);

        List<FluxRecord> records = fluxTable.getRecords();
        for (FluxRecord record : records) {
            carList.add(this.carFromRecord(record));
        }

        // TODO: make sure to close the client. However you do that
        client.close();
        return carList;
    }

    /**
     * Create a new car and add it to the influx database. Note an entry in influxdb has
     * to have at least one field and value pair. Any car added will include the rpm field 
     * set to 0.0. The Car object will be returned.
     * 
     * @param newCar Car object to write to the database
     * @return {@link Car} newCar
     */
    public Car saveNewCar(Car newCar){
        InfluxDBClient client = this.getClient();

        Point point = Point
            .measurement(InfluxDBConnectionFactory.MEASUREMENT)
            .addTag("make", newCar.getMake())
            .addTag("model", newCar.getModel())
            .addTag("year", String.valueOf(newCar.getYear()))
            .addTag("id", String.valueOf(newCar.getId()))
            .addField("rpm", 0.0) // one field must be present to write a point
            .time(Instant.now(), WritePrecision.MS);

        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writePoint(InfluxDBConnectionFactory.CAR_BUCKET, InfluxDBConnectionFactory.ORG, point);

        System.out.println("NEW CAR");
        System.out.println(newCar);
        client.close();
        return newCar;
    }

    private InfluxDBClient getClient(){
        return InfluxDBConnectionFactory.create();
    }

    /**
     * Uses the information in a provided record to construct a Car object
     * 
     * @param record {@link FluxRecord}
     * @return {@link Car} object
     */
    private Car carFromRecord(FluxRecord record){
        // TODO add checking that these keys actually exists
        String make = (String) record.getValueByKey("make");
        String model = (String) record.getValueByKey("model");
        int year = Integer.valueOf((String) record.getValueByKey("year"));
        int id = Integer.valueOf(((String) record.getValueByKey("id")));
        return new Car(make, model, year, id);
    }


    /**
     * Return a {@link Car} object for the car with the given Id.
     * 
     * @param id
     * @return {@link Car}
     */
    public Car findById(Integer id) {
        // returning a null car if not car is found is not the best solution
        Car car = null;
        
        InfluxDBClient client = this.getClient();

        // grouping by measurement puts all results in the same fluxtable which matches the data representation we expect
        String query = """
                        from(bucket: \"%s\")
                        |> range(start: -1y)
                        |> group(columns: [\"_measurement\"])
                        |> filter(fn: (r) => r[\"_measurement\"] == \"carTelemetry\")
                        |> filter(fn: (r) => r[\"id\"] == \"%d\")
                        |> keep(columns: [\"id\", \"make\", \"model\", \"year\"])
                        |> unique(column: \"id\")
                        """.formatted(InfluxDBConnectionFactory.CAR_BUCKET, id);

        FluxTable fluxTable = client.getQueryApi().query(query, InfluxDBConnectionFactory.ORG).get(0);
        List<FluxRecord> records = fluxTable.getRecords();

        // this can be done more safely
        // I'm making assumptions that the query will always return one table with one record
        if(!records.isEmpty()){
            car = this.carFromRecord(records.get(0));
        }
        
        client.close();
        return car;
    }   
}
