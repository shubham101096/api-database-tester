package com.project.apidbtester.testapis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ColumnResult contains the result of the test performed on a particular column of a table
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnResult {
    private String columnName;
    private String expectedValue;
    private String actualValue;
    private Boolean passed;
}
