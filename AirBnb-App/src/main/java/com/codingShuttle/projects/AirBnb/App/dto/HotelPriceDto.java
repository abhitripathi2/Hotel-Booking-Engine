package com.codingShuttle.projects.AirBnb.App.dto;

import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    private Hotel hotel;
    private Double price;


}
