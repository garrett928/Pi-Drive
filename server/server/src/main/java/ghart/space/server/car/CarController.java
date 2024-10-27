package ghart.space.server.car;

import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
class CarController {
    private final CarRepository repository;
    private final CarModelAssembler assembler;
    private CarDBHelper dbHelper;

    CarController(CarRepository repository,  CarModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
    this.dbHelper = new CarDBHelper();
    }


    // Aggregate root
    @GetMapping("/cars")
    CollectionModel<EntityModel<Car>> all() {
        List<EntityModel<Car>> cars = dbHelper.findAllCars().stream()
            .map(assembler::toModel) //
            .collect(Collectors.toList());

        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).all()).withSelfRel());
    }

    @PostMapping("/cars")
    ResponseEntity<?> newCar(@RequestBody Car newCar) {
        EntityModel<Car> entityModel = assembler.toModel(dbHelper.saveNewCar(newCar));
        return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }

    // Single item
	@GetMapping("/cars/{id}")
	EntityModel<Car> one(@PathVariable Integer id) {

        Car car = dbHelper.findById(id);
        if(car == null){ throw new CarNotFoundException(id); }
		return assembler.toModel(car);
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
