package com.gm.mcayambe.core.util;

import com.gm.mcayambe.core.model.Car;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UtilsCar {
    private static Car create(int i) {
        return new Car(i).model("model " + i).name("name" + i).price(Double.valueOf(i));
    }
    private List<Car> cars;
    @PostConstruct
    public void init() {
        cars = new ArrayList<>();
        IntStream.rangeClosed(1, 50)
                .forEach(i -> cars.add(create(i)));
    }

    @Produces
    public List<Car> getCars() {
        return cars;
    }
}
