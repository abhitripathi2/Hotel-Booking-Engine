package com.codingShuttle.projects.HotelManagement.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoDto {

    private HotelDto hotel;
    private List<RoomDto> rooms;


}
