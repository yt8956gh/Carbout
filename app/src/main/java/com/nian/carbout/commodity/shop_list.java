package com.nian.carbout.commodity;

public class shop_list {


    private String name;
    private float number;
    private int co2;

    public shop_list(String name,float number)
    {
        this.name = name;
        this.number = number;
        co2=0;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getCo2() {
        return co2;
    }

    public String getName() {
        return name;
    }

    public float getNumber() {
        return number;
    }
}
