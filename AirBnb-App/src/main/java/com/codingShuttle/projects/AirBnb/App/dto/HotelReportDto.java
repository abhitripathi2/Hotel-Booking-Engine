package com.codingShuttle.projects.AirBnb.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelReportDto {

    private Long bookingCount;
    private BigDecimal totalRevenue;
    private BigDecimal averageRevenue;

}
