package ghart.space.server.car;

import java.util.List;
import java.util.ArrayList;

import com.influxdb.client.InfluxDBClient;
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
            String make = (String) record.getValueByKey("make");
            String model = (String) record.getValueByKey("model");
            int year = (int) record.getValueByKey("year");
            int id = Integer.valueOf(((String) record.getValueByKey("id")));
            Car car = new Car(make, model, year, id);
            carList.add(car);
        }

        // TODO: make sure to close the client. However you do that
        // client.close
        return carList;
    }

    public Car saveNewCar(Car newCar){
        InfluxDBClient client = this.getClient();
        System.out.println("NEW CAR");
        System.out.println(newCar);
        return new Car("honda", "fit", 2008);
    }

    private InfluxDBClient getClient(){
        return InfluxDBConnectionFactory.create();
    }   
}
