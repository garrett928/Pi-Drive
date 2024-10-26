package ghart.space.server;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class CarModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

@Override
public EntityModel<Car> toModel(Car car) {

    return EntityModel.of(car, //
        linkTo(methodOn(CarController.class).one(car.getId())).withSelfRel(),
        linkTo(methodOn(CarController.class).all()).withRel("cars"));
    }
}
