package com.springbatch.batch.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBean {
    private String name;
    private String rollNo;
    private String department;
    private String result;
    private Double cgpa;
    private String distinction;

}
