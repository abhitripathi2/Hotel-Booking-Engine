package com.codingShuttle.projects.AirBnb.App.strategy;

import com.codingShuttle.projects.AirBnb.App.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HolidaysPricingStrategy implements PricingStrategy{

    @Qualifier("basePricingStrategy")
    private final PricingStrategy wrapped;
    
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        boolean isTodayHoliday = true; // TODO: Replace with actual holiday check logic. call an API or check with local data.
        if (isTodayHoliday) {
            price = price.multiply(new BigDecimal("1.25"));
        }
        return price;
    }
}
