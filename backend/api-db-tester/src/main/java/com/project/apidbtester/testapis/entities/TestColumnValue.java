package com.project.apidbtester.testapis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TestColumnValue contains the column details and its expected value to be tested by the tester
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
