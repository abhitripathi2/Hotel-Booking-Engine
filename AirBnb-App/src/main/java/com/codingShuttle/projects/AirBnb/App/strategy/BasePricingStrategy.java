package com.codingShuttle.projects.AirBnb.App.strategy;

import com.codingShuttle.projects.AirBnb.App.entity.Inventory;

import java.math.BigDecimal;


public class BasePricingStrategy implements PricingStrategy {


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

        return inventory.getRoom().getBasePrice();
    }
}
