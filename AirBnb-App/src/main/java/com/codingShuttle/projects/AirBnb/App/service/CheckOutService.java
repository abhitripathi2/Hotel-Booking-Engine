package com.codingShuttle.projects.AirBnb.App.service;


import com.codingShuttle.projects.AirBnb.App.entity.Booking;

public interface CheckOutService {

    String getCheckOutSession(Booking booking, String successUrl, String failureUrl);

}
