package ghart.space.server.car;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(Integer id) {
    super("Could not find car " + id);
    }
}
