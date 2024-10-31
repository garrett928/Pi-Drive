package ghart.space.server.apiController;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ghart.space.server.car.Car;

@Component
class CarModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

@Override
public EntityModel<Car> toModel(Car car) {

    return EntityModel.of(car,
        linkTo(methodOn(APIController.class).one(car.getId())).withSelfRel(),
        linkTo(methodOn(APIController.class).all()).withRel("cars"));
    }
}
