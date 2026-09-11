package com.codingShuttle.projects.AirBnb.App.repository;

import com.codingShuttle.projects.AirBnb.App.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

}
