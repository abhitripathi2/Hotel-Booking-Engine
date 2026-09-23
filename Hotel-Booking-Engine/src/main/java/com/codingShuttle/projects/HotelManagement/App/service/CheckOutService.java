package com.codingShuttle.projects.HotelManagement.App.service;


import com.codingShuttle.projects.HotelManagement.App.entity.Booking;

public interface CheckOutService {

    String getCheckOutSession(Booking booking, String successUrl, String failureUrl);

}
