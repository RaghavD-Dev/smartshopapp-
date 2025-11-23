package com.example.smart_shop.service.dto;

import java.math.BigDecimal;

public class CartInsights {

    private BigDecimal averagePrice;
    private String dominantCategory;
    private BigDecimal totalSavings;

    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }

    public String getDominantCategory() { return dominantCategory; }
    public void setDominantCategory(String dominantCategory) { this.dominantCategory = dominantCategory; }

    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }
}
