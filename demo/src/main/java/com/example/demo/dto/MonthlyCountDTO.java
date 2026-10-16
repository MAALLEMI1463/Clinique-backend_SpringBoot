package com.example.demo.dto;

/**
 * One data point on the monthly consultations line chart.
 * month: 1–12, count: number of consultations that month.
 */
public class MonthlyCountDTO {

    private int month;      // 1 = January … 12 = December
    private int year;
    private long count;

    public MonthlyCountDTO() {}

    public MonthlyCountDTO(int month, int year, long count) {
        this.month = month;
        this.year  = year;
        this.count = count;
    }

    public int getMonth()   { return month; }
    public int getYear()    { return year; }
    public long getCount()  { return count; }

    public void setMonth(int month)  { this.month = month; }
    public void setYear(int year)    { this.year = year; }
    public void setCount(long count) { this.count = count; }
}