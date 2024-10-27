package ghart.space.server.car;

class CarNotFoundException extends RuntimeException {

    CarNotFoundException(Integer id) {
    super("Could not find car " + id);
    }
}
