package com.codingShuttle.projects.AirBnb.App.repository;

import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


}
