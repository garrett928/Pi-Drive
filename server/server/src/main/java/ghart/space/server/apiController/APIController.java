package ghart.space.server.apiController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ghart.space.server.car.Car;
import ghart.space.server.car.CarNotFoundException;
import ghart.space.server.car.CarDBHelper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
class APIController {
    private final CarModelAssembler assembler;
    private CarDBHelper dbHelper;

    APIController(CarModelAssembler assembler) {
    this.assembler = assembler;
    this.dbHelper = new CarDBHelper();
    }


    // get all cars
    @GetMapping("/cars")
    CollectionModel<EntityModel<Car>> all() {
        List<EntityModel<Car>> cars = dbHelper.findAllCars().stream()
            .map(assembler::toModel) //
            .collect(Collectors.toList());

        return CollectionModel.of(cars, linkTo(methodOn(APIController.class).all()).withSelfRel());
    }

    // add a car
    @PostMapping("/cars")
    ResponseEntity<?> newCar(@RequestBody Car newCar) {
        EntityModel<Car> entityModel = assembler.toModel(dbHelper.saveNewCar(newCar));
        return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }

    // get a single car
	@GetMapping("/cars/{id}")
	EntityModel<Car> one(@PathVariable Integer id) {

        Car car = dbHelper.findById(id);
        if(car == null){ throw new CarNotFoundException(id); }
		return assembler.toModel(car);
	}

    // add a piece of telemetry data to a car
    @PostMapping("/cars/{id}/telemetry")
    ResponseEntity<?> newCarTelemetry(@PathVariable Integer id) {

        // check the car exist first
        Car car = dbHelper.findById(id);
        if(car == null){ throw new CarNotFoundException(id); }

        // respond with the /car/{id} endpoint for the car which we just added data for
        // a more useful response would be sending the data back
        EntityModel<Car> entityModel = assembler.toModel(car);
        return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }


    // Do not allow updating a single car. The entries are allready written to the database
    // @PutMapping("/cars/{id}")
    // ResponseEntity<?> replaceCar(@RequestBody Car newCar, @PathVariable int id) {
    
    //   Car updatedCar = repository.findById(id) //
    //         .map(car -> {
    //             car.setMake(newCar.getMake());
    //             car.setModel(newCar.getModel());
    //             car.setYear(newCar.getYear());
    //             return repository.save(car);
    //         })
    //         .orElseGet(() -> {
    //             return repository.save(newCar);
    //         });
        
    //     EntityModel<Car> entityModel = assembler.toModel(updatedCar);
        
    //     return ResponseEntity //
    //         .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
    //         .body(entityModel);
    // }

    
    // could allow for deleting entries in the future
//     @DeleteMapping("/cars/{id}")
//     ResponseEntity<?> deleteCar(@PathVariable Integer id) {

//         repository.deleteById(id);

//         return ResponseEntity.noContent().build();
// }
}
