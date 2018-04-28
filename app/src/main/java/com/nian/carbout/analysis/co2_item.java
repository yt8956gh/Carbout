package com.nian.carbout.analysis;

public class co2_item {
    private String name;
    private int date;
    private int co2;
    private long key;

    public co2_item(String name, int date, int co2, long key)
    {
        this.name=name;
        this.date=date;
        this.co2=co2;
        this.key=key;
    }

    // 讀取與設定欄位變數的方法
    public long getKey() { return key; }

    public void setKey(long key) { this.key = key; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCO2() { return co2; }

    public void setCO2(int co2) {this.co2 = co2; }

    public int getDate() { return date; }

    public void setDate(int date) { this.date = date; }

    @Override
    public String toString() {
        return (getDate()+":"+getName()+":"+getCO2());
    }
}