package com.nian.carbout.commodity_Search;

public class list_item {

    private String name;
    private float co2;
    private String unit;

    public list_item(String name,float co2,String unit)
    {
        this.name = name;
        this.co2 = co2;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public float getCo2() {
        return co2;
    }

    public String getUnit() {
        return unit;
    }
}
