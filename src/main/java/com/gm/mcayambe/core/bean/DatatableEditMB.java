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
package com.gm.mcayambe.core.bean;

import com.gm.mcayambe.core.model.Car;
import com.gm.mcayambe.core.service.CarService;
import org.omnifaces.cdi.ViewScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

@Named
@ViewScoped
public class DatatableEditMB implements Serializable {

    private List<Car> cars1;
    private List<Car> cars2;

    @Inject
    private CarService service;

    @PostConstruct
    public void init() {
        cars1 = service.createCars(10);
        cars2 = service.createCars(10);
    }

    public List<Car> getCars1() {
        return cars1;
    }

    public List<Car> getCars2() {
        return cars2;
    }

    public List<String> getBrands() {
        return service.getBrands();
    }

    public List<String> getColors() {
        return service.getColors();
    }

    public void setService(CarService service) {
        this.service = service;
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Car Edited", ((Car) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
