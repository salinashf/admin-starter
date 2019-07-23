/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gm.mcayambe.core.service;


import com.github.adminfaces.template.exception.BusinessException;
import com.gm.mcayambe.core.infra.model.Filter;
import com.gm.mcayambe.core.infra.model.SortOrder;
import com.gm.mcayambe.core.model.Car;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.adminfaces.template.util.Assert.has;


public class CarService implements Serializable {

	@Inject
	List<Car> allCars;

    private final static String[] colors;
	
	private final static String[] brands;

    static {
		colors = new String[10];
		colors[0] = "Black";
		colors[1] = "White";
		colors[2] = "Green";
		colors[3] = "Red";
		colors[4] = "Blue";
		colors[5] = "Orange";
		colors[6] = "Silver";
		colors[7] = "Yellow";
		colors[8] = "Brown";
		colors[9] = "Maroon";
		
		brands = new String[10];
		brands[0] = "BMW";
		brands[1] = "Mercedes";
		brands[2] = "Volvo";
		brands[3] = "Audi";
		brands[4] = "Renault";
		brands[5] = "Fiat";
		brands[6] = "Volkswagen";
		brands[7] = "Honda";
		brands[8] = "Jaguar";
		brands[9] = "Ford";
	}
    
    public List<Car> createCars(int size) {
        List<Car> list = new ArrayList<Car>();
		for(int i = 0 ; i < size ; i++) {
			list.add(new Car(getRandomId(), getRandomBrand(), getRandomYear(), getRandomColor(), getRandomPrice(), getRandomSoldState()));
        }
        
        return list;
    }
    
    private String getRandomId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
    
    private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}
	
	private String getRandomColor() {
		return colors[(int) (Math.random() * 10)];
	}
	
	private String getRandomBrand() {
		return brands[(int) (Math.random() * 10)];
	}
    
    private int getRandomPrice() {
		return (int) (Math.random() * 100000);
	}
    
    private boolean getRandomSoldState() {
		return (Math.random() > 0.5) ? true: false;
	}
    
    public List<String> getColors() {
        return Arrays.asList(colors);
    }
    
    public List<String> getBrands() {
        return Arrays.asList(brands);
    }



	public List<Car> listByModel(String model) {
		return allCars.stream()
				.filter(c -> c.getModel().equalsIgnoreCase(model))
				.collect(Collectors.toList());

	}

	public List<Car> paginate(Filter<Car> filter) {
		List<Car> pagedCars = new ArrayList<>();
		if(has(filter.getSortOrder()) && !SortOrder.UNSORTED.equals(filter.getSortOrder())) {
			pagedCars = allCars.stream().
					sorted((c1, c2) -> {
						if (filter.getSortOrder().isAscending()) {
							return c1.getId().compareTo(c2.getId());
						} else {
							return c2.getId().compareTo(c1.getId());
						}
					})
					.collect(Collectors.toList());
		}

		int page = filter.getFirst() + filter.getPageSize();
		if (filter.getParams().isEmpty()) {
			pagedCars = pagedCars.subList(filter.getFirst(), page > allCars.size() ? allCars.size() : page);
			return pagedCars;
		}

		List<Predicate<Car>> predicates = configFilter(filter);

		List<Car> pagedList = allCars.stream().filter(predicates
				.stream().reduce(Predicate::or).orElse(t -> true))
				.collect(Collectors.toList());

		if (page < pagedList.size()) {
			pagedList = pagedList.subList(filter.getFirst(), page);
		}

		if (has(filter.getSortField())) {
			pagedList = pagedList.stream().
					sorted((c1, c2) -> {
						boolean asc = SortOrder.ASCENDING.equals(filter.getSortOrder());
						if (asc) {
							return c1.getId().compareTo(c2.getId());
						} else {
							return c2.getId().compareTo(c1.getId());
						}
					})
					.collect(Collectors.toList());
		}
		return pagedList;
	}

	private List<Predicate<Car>> configFilter(Filter<Car> filter) {
		List<Predicate<Car>> predicates = new ArrayList<>();
		if (filter.hasParam("id")) {
			Predicate<Car> idPredicate = (Car c) -> c.getId().equals(filter.getParam("id"));
			predicates.add(idPredicate);
		}

		if (filter.hasParam("minPrice") && filter.hasParam("maxPrice")) {
			Predicate<Car> minMaxPricePredicate = (Car c) -> c.getPrice()
					>= Double.valueOf((String) filter.getParam("minPrice")) && c.getPrice()
					<= Double.valueOf((String) filter.getParam("maxPrice"));
			predicates.add(minMaxPricePredicate);
		} else if (filter.hasParam("minPrice")) {
			Predicate<Car> minPricePredicate = (Car c) -> c.getPrice()
					>= Double.valueOf((String) filter.getParam("minPrice"));
			predicates.add(minPricePredicate);
		} else if (filter.hasParam("maxPrice")) {
			Predicate<Car> maxPricePredicate = (Car c) -> c.getPrice()
					<= Double.valueOf((String) filter.getParam("maxPrice"));
			predicates.add(maxPricePredicate);
		}

		if (has(filter.getEntity())) {
			Car filterEntity = filter.getEntity();
			if (has(filterEntity.getModel())) {
				Predicate<Car> modelPredicate = (Car c) -> c.getModel().toLowerCase().contains(filterEntity.getModel().toLowerCase());
				predicates.add(modelPredicate);
			}

			if (has(filterEntity.getPrice())) {
				Predicate<Car> pricePredicate = (Car c) -> c.getPrice() ==filterEntity.getPrice();
				predicates.add(pricePredicate);
			}

			if (has(filterEntity.getName())) {
				Predicate<Car> namePredicate = (Car c) -> c.getName().toLowerCase().contains(filterEntity.getName().toLowerCase());
				predicates.add(namePredicate);
			}
		}
		return predicates;
	}

	public List<String> getModels(String query) {
		return allCars.stream().filter(c -> c.getModel()
				.toLowerCase().contains(query.toLowerCase()))
				.map(Car::getModel)
				.collect(Collectors.toList());
	}

	public void insert(Car car) {
		validate(car);
		car.setIdI(allCars.stream()
				.mapToInt(c -> c.getIdI())
				.max()
				.getAsInt()+1);
		allCars.add(car);
	}

	public void validate(Car car) {
		BusinessException be = new BusinessException();
		if (!car.hasModel()) {
			be.addException(new BusinessException("Car model cannot be empty"));
		}
		if (!car.hasName()) {
			be.addException(new BusinessException("Car name cannot be empty"));
		}

		if (!has(car.getPrice())) {
			be.addException(new BusinessException("Car price cannot be empty"));
		}

		if (allCars.stream().filter(c -> c.getName().equalsIgnoreCase(car.getName())
				&& c.getId() != c.getId()).count() > 0) {
			be.addException(new BusinessException("Car name must be unique"));
		}
		if(has(be.getExceptionList())) {
			throw be;
		}
	}


	public void remove(Car car) {
		allCars.remove(car);
	}

	public long count(Filter<Car> filter) {
		return allCars.stream()
				.filter(configFilter(filter).stream()
						.reduce(Predicate::or).orElse(t -> true))
				.count();
	}

	public Car findById(Integer id) {
		return allCars.stream()
				.filter(c -> c.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new BusinessException("Car not found with id " + id));
	}

	public void update(Car car) {
		validate(car);
		allCars.remove(allCars.indexOf(car));
		allCars.add(car);
	}
}
