package ghart.space.server.car;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Car {

    private @Id @GeneratedValue Long id;
    private String make;
    private String model;
    private String yr; // year is a reserved word in SQL

    public Car() {}

    public Car(String make, String model, String year) {

        this.make = make;
        this.model = model;
        this.yr = year;
    }

    public String getName() {
        return this.make + " " + this.model;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.make = parts[0];
        this.model = parts[1];
    }

    public Long getId() {
        return this.id;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public String getYr() {
        return this.yr;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYr(String role) {
        this.yr = role;
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
        return Objects.hash(this.id, this.make, this.model, this.yr);
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + this.id + ", make='" + this.make + '\'' + ", model='" + this.model
            + '\'' + ", year='" + this.yr + '\'' + '}';
    }
}
