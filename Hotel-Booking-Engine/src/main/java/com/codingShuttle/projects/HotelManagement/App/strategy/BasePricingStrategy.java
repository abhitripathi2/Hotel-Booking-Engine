package com.codingShuttle.projects.HotelManagement.App.strategy;

import com.codingShuttle.projects.HotelManagement.App.entity.Inventory;

import java.math.BigDecimal;


public class BasePricingStrategy implements PricingStrategy {


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

        return inventory.getRoom().getBasePrice();
    }
}
