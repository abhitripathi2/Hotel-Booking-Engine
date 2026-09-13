package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.entity.Hotel;
import com.codingShuttle.projects.AirBnb.App.entity.HotelMinPrice;
import com.codingShuttle.projects.AirBnb.App.entity.Inventory;
import com.codingShuttle.projects.AirBnb.App.repository.HotelMinPriceRepository;
import com.codingShuttle.projects.AirBnb.App.repository.HotelRepository;
import com.codingShuttle.projects.AirBnb.App.repository.InventoryRepository;
import com.codingShuttle.projects.AirBnb.App.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateServices {

    //scheduler to update the inventory and HotelMinPrice tables every hour

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;


    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void updatePrices(){
        int page = 0;
        int batchSize = 100;
        while (true) {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if (hotelPage.isEmpty()) {
                break;
            }

            hotelPage.getContent().forEach(this::updateHotelPrices);
            page++;
        }

    }
    private void updateHotelPrices(Hotel hotel) {
        log.info("Updating prices for hotel ID: {}", hotel.getId());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findAllByHotelAndDateBetween(hotel, startDate, endDate);

        log.info(
                "Hotel ID: {}, Inventory records found: {}",
                hotel.getId(),
                inventoryList.size()
        );

        updateInventoryPrice(inventoryList);
        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {


        //compute the minimum price for each date and store it in a map
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().orElse(BigDecimal.ZERO)
                ));

        //prepare hotel price entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        //save all hotel prices in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);



    }

    private void updateInventoryPrice(List<Inventory> inventoryList) {
        // Implement your logic to update the price of the inventory based on your pricing strategy
        // For example, you can use the PricingStrategy interface and its implementations to calculate the new price
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);

    }



}
