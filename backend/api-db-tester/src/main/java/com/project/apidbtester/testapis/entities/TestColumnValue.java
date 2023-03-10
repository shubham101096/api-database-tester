package com.project.apidbtester.testapis.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TestColumnValue {
    @Id
    @GeneratedValue
    private Integer id;
    private String columnName;
    private String expectedValue;
    private String actualValue;
    private Boolean passed=false;
    @ManyToOne
    @JoinColumn(name = "test_case_details_id", referencedColumnName = "id")
    private TestCaseDetails testCaseDetails;
}
