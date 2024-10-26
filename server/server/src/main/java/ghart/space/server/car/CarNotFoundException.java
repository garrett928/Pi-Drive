package ghart.space.server.car;

class CarNotFoundException extends RuntimeException {

    CarNotFoundException(Long id) {
    super("Could not find car " + id);
    }
}
