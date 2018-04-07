package com.nian.carbout;

public class co2_item {
    private String name;
    private String date;
    private int co2;
    private long key;

    public co2_item(String name, String date, int co2, long key)
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

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return String.format("%d, %s, %b", getKey(), getName(), getCO2());
    }
}