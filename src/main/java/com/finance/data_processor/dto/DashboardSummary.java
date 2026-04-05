package com.finance.data_processor.dto;

import lombok.Data;

@Data
public class DashboardSummary {
    private Double totalIncome = 0.0;
    private Double totalExpense = 0.0;
    private Double netBalance = 0.0;

}
