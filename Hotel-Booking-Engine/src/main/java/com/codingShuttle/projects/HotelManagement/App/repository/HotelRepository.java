package com.codingShuttle.projects.HotelManagement.App.repository;

import com.codingShuttle.projects.HotelManagement.App.entity.Hotel;
import com.codingShuttle.projects.HotelManagement.App.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


    List<Hotel> findByOwner(User user);
}
