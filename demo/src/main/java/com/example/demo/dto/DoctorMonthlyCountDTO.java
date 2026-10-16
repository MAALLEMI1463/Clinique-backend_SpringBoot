package com.example.demo.dto;

/**
 * One data point for the per-doctor monthly line chart.
 * Groups by doctorId + doctorName + month + year + count.
 */
public class DoctorMonthlyCountDTO {

    private Long   doctorId;
    private String doctorName;
    private int    month;
    private int    year;
    private long   count;

    public DoctorMonthlyCountDTO() {}

    public DoctorMonthlyCountDTO(Long doctorId, String doctorName,
                                  int month, int year, long count) {
        this.doctorId   = doctorId;
        this.doctorName = doctorName;
        this.month      = month;
        this.year       = year;
        this.count      = count;
    }

    public Long   getDoctorId()   { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public int    getMonth()      { return month; }
    public int    getYear()       { return year; }
    public long   getCount()      { return count; }

    public void setDoctorId(Long v)    { this.doctorId = v; }
    public void setDoctorName(String v){ this.doctorName = v; }
    public void setMonth(int v)        { this.month = v; }
    public void setYear(int v)         { this.year = v; }
    public void setCount(long v)       { this.count = v; }
}