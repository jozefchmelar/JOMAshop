package com.joma.jomashop.dto;

/**
 * Created by mgana on 07.12.2015.
 */
public class StatisticDTO {
    private int id;
    private String name;
    private float total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
