package ghart.space.server.car;

import java.io.IOException;
import java.util.Objects;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.boot.json.JsonParser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@JsonComponent
public class Car {

    // our car ID will simply be the hash of the make, model, and year
    // This has the inherit problem of only allowing one car of a make, model, and year at a time
    // The car VIN might be a more useful ID in the future
    // However, this does make the initally implementation easier to get off the ground
    private @Id int id;
    private String make;
    private String model;
    // year is a reserved word in SQL and therefore influxDB. Cannot be used as a field
    private int yr; 

    public Car() {}

    public Car(String make, String model, int year) {

        this.make = make;
        this.model = model;
        this.yr = year;
        this.id = this.hashCode();
    }

    public Car(String make, String model, int year, int id) {

        this.make = make;
        this.model = model;
        this.yr = year;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public int getYr() {
        return this.yr;
    }

    public void setId(int id) {
        this.id = this.hashCode();
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYr(int yr) {
        this.yr = yr;
    }

    
    @Override
    public boolean equals(Object o) {
        
        if (this == o)
        return true;
        if (!(o instanceof Car))
        return false;
        Car car = (Car) o;
        return Objects.equals(this.id, car.id) && Objects.equals(this.make, car.make)
        && Objects.equals(this.model, car.model) && Objects.equals(this.yr, car.yr);
    }
    
    @Override
    public int hashCode() {
        // Because I am using the hashCode as the vehical ID I don't want a negative ID in the URL
        // bitwise and to clear the sign bit
        // note: this also clears the next three MSB bits but that is ok for this application 
        // https://stackoverflow.com/a/13422442 useful comment showing signed, vs ones complement, vs twos complement

        return Objects.hash(this.make, this.model, this.yr) & 0xfffffff;
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + this.id + ", make='" + this.make + '\'' + ", model='" + this.model
            + '\'' + ", year='" + this.yr + '\'' + '}';
    }

    /**
     * Overrive the default deserializer behavior. 
     * We do not want the deserializer to use the ID field. This field shold be
     * populated by the car's hashcode.
     */
	public static class Deserializer extends JsonObjectDeserializer<Car> {

        @Override
        protected Car deserializeObject(com.fasterxml.jackson.core.JsonParser jsonParser,
                DeserializationContext context, ObjectCodec codec, JsonNode tree) throws IOException {
                
                String make = nullSafeValue(tree.get("make"), String.class);
                String model = nullSafeValue(tree.get("model"), String.class);
                int year = nullSafeValue(tree.get("year"), Integer.class);
                return new Car(make, model, year);
        }

	}
}
