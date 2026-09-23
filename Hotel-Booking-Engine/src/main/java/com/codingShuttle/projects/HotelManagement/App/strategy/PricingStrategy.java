package com.codingShuttle.projects.HotelManagement.App.strategy;

import com.codingShuttle.projects.HotelManagement.App.entity.Inventory;

import java.math.BigDecimal;


public interface PricingStrategy {



    BigDecimal calculatePrice(Inventory inventory);


}
