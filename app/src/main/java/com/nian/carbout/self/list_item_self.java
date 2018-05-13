package com.nian.carbout.self;

public class list_item_self {

    private String name;
    private float co2;
    private String unit;

    public list_item_self(String name, float co2, String unit)
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
