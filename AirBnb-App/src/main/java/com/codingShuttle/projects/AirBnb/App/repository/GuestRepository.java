package com.codingShuttle.projects.AirBnb.App.repository;

import com.codingShuttle.projects.AirBnb.App.entity.Guest;
import com.codingShuttle.projects.AirBnb.App.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByUser(User user);

}
