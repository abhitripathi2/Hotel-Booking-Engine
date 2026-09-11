package com.codingShuttle.projects.AirBnb.App.strategy;

import com.codingShuttle.projects.AirBnb.App.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

    @Qualifier("basePricingStrategy")
    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = inventory.getBookedCount() / (double) inventory.getTotalCount();
        if (occupancyRate > 0.8) {
            price = price.multiply(BigDecimal.valueOf(1.2)); // Increase price by 20% if occupancy is above 80%
        }
        return price;

    }
}
