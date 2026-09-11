package com.codingShuttle.projects.AirBnb.App.repository;

import com.codingShuttle.projects.AirBnb.App.dto.HotelPriceDto;
import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import com.codingShuttle.projects.AirBnb.App.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {

    @Query("""
            SELECT new com.codingShuttle.projects.AirBnb.App.dto.HotelPriceDto(i.hotel, AVG(i.price))
            FROM HotelMinPrice i
            WHERE i.hotel.city = :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.hotel.isActive = true
            GROUP BY i.hotel
            """)
    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") int roomsCount,
            @Param("dateCount") long dateCount,
            Pageable pageable
    );



    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
