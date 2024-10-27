package ghart.space.server.car;

import java.util.List;
import java.util.ArrayList;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import ghart.space.server.InfluxDBConnectionFactory;

public class CarDBHelper {

    public CarDBHelper(){

    }

    public List<Car> findAllCars(){
        InfluxDBClient client = this.getClient();
        String query = """
                        from(bucket: \"car-test\")
                        |> range(start: -1y)
                        |> group(columns: [\"_measurement\"])
                        |> filter(fn: (r) => r[\"_measurement\"] == \"carTelemetry\")
                        |> keep(columns: [\"id\", \"make\", \"model\", \"year\"])
                        |> unique(column: \"id\")
                        """;
    FluxTable fluxTable = client.getQueryApi().query(query, InfluxDBConnectionFactory.ORG)[0];

        List<FluxRecord> records = fluxTable.getRecords();
        for (FluxRecord record : records) {
            String make = record.getValueByKey("make")
            String model = record.getValueByKey("model")
            String year = record.getValueByKey("year")
            String id = record.getValueByKey("id")
            Car car = new Car()
        }
    

    List<Car> list = new ArrayList<Car>();
    // client.close
    // make sure to close the client. However you do that
    return list;
}



    private InfluxDBClient getClient(){
        return InfluxDBConnectionFactory.create();
    }

    
}
