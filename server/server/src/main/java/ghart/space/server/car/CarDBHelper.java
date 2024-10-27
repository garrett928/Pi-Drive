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
        String query = "from(bucket: \"car-test\") |> range(start: -1y)" ;
    List<FluxTable> tables = client.getQueryApi().query(query, InfluxDBConnectionFactory.ORG);

    for (FluxTable table : tables) {
    for (FluxRecord record : table.getRecords()) {
        System.out.println(record);
    }
}

    List<Car> list = new ArrayList<Car>();

    return list;
}



    private InfluxDBClient getClient(){
        return InfluxDBConnectionFactory.create();
    }

    
}
