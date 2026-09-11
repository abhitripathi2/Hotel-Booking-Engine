package com.codingShuttle.projects.AirBnb.App.strategy;

import com.codingShuttle.projects.AirBnb.App.entity.Inventory;

import java.math.BigDecimal;


public interface PricingStrategy {



    BigDecimal calculatePrice(Inventory inventory);


}
