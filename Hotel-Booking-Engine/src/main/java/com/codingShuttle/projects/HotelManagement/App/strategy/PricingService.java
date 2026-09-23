package com.codingShuttle.projects.HotelManagement.App.strategy;

import com.codingShuttle.projects.HotelManagement.App.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory) {

        PricingStrategy pricingStrategy = new BasePricingStrategy();

        // Apply additional pricing strategies based on inventory attributes
        pricingStrategy = new SurgePricingStrategy(pricingStrategy);
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
        pricingStrategy = new HolidaysPricingStrategy(pricingStrategy);

        return pricingStrategy.calculatePrice(inventory);
    }

    //Return the sum of all the prices of the inventory list
    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList) {
        return inventoryList.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}