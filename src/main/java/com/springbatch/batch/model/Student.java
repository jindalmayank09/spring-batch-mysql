package com.springbatch.batch.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name ="name")
    private String name;
    @Column(name ="roll_no")
    private String rollNo;
    @Column(name ="department")
    private String department;
    @Column(name ="result")
    private String result;
    @Column(name ="cgpa")
    private Double cgpa;
    @Column(name ="distinction")
    private String distinction;
}
