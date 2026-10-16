package com.example.demo.dto;

import java.util.List;

/**
 * Wraps a paginated list of patients with pagination metadata.
 */
public class PatientPageResponseDTO {

    private List<PatientResponseDTO> patients;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean first;
    private boolean last;

    public PatientPageResponseDTO() {}

    public PatientPageResponseDTO(List<PatientResponseDTO> patients, int currentPage,
                                   int totalPages, long totalElements,
                                   int pageSize, boolean first, boolean last) {
        this.patients = patients;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.first = first;
        this.last = last;
    }

    public List<PatientResponseDTO> getPatients() { return patients; }
    public void setPatients(List<PatientResponseDTO> patients) { this.patients = patients; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
}