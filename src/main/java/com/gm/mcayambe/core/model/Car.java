package com.gm.mcayambe.core.model;

import java.io.Serializable;

/**
 * Created by rmpestano on 18/01/17.
 */
public class Car implements Serializable  , Comparable<Car>{

    public String id;
    public String brand;
    public Integer year;
    public String color;
    public int price;
    public boolean sold;
    private String model;
    private String name;
    public Car() {}

    public Car(String id, String brand, Integer year, String color, int price, boolean sold) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.color = color;
        this.price = price;
        this.sold = sold;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdI() {
        return Integer.parseInt(id);
    }

    public void setIdI(int id) {
        this.id = String.valueOf(id);
    }


    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }



    public boolean isSold() {
        return sold;
    }
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Car other = (Car) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }




    public Car(Integer id) {
        this.id = String.valueOf(id);
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public boolean hasModel() {
        return model != null && !"".equals(model.trim());
    }

    public boolean hasName() {
        return name != null && !"".equals(name.trim());
    }
    public Car model(String model) {
        this.model = model;
        return this;
    }


    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public void setPrice(int price) {
        this.price = price;
    }

    public Car price(int price) {
        this.price = price;
        return this;
    }
    public Car price(double price) {
        this.price =(int)price ;
        return this;
    }

    public Car name(String name) {
        this.name = name;
        return this;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public int compareTo(Car o) {
        return this.id.compareTo(o.getId());
    }

}