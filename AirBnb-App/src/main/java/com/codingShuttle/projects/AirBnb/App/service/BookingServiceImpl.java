package com.codingShuttle.projects.AirBnb.App.service;

import com.codingShuttle.projects.AirBnb.App.dto.BookingDto;
import com.codingShuttle.projects.AirBnb.App.dto.BookingRequest;
import com.codingShuttle.projects.AirBnb.App.dto.GuestDto;
import com.codingShuttle.projects.AirBnb.App.entity.*;
import com.codingShuttle.projects.AirBnb.App.entity.enums.BookingStatus;
import com.codingShuttle.projects.AirBnb.App.exception.ResourceNotFoundException;
import com.codingShuttle.projects.AirBnb.App.exception.UnAuthorisedException;
import com.codingShuttle.projects.AirBnb.App.repository.*;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutService checkOutService;

    @Value("${frontend.url}")
    private String frontendUrl;


    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {

        log.info("Initialising booking for hotel: {}, room: {}, check-in: {}, check-out: {}",
                bookingRequest.getHotelId(),
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + bookingRequest.getRoomId()));


        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount()
        );

        Long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) +1;

        if(inventoryList.size() != daysCount) {
            throw new IllegalStateException("Not enough available rooms for the selected dates.");
        }

        //Reserve the rooms by updating the bookedCount
        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //create a booking

        Booking booking =  Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);


    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {

        log.info("Adding guests for booking with ID: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        User user = getCurrentUser();


        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking do not belong to the current user with ID: " + user.getUser_id());
        }

        if(hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has expired. Please initiate a new booking.");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Guests can only be added to reserved bookings.");
        }

        for(GuestDto guestDto : guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);

        booking = bookingRepository.save(booking);


        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public String initiatePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking do not belong to the current user with ID: " + user.getUser_id());
        }
        if(hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has expired. Please initiate a new booking.");
        }

        String sessionUrl = checkOutService.getCheckOutSession(booking,
                frontendUrl+"/payments/success", frontendUrl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayments(Event event) {
        if("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if(session == null) return;

            String sessionId = session.getId();
            Booking booking = bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                                new ResourceNotFoundException("Booking not found for payment session ID: " + sessionId));
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),
                        booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
            inventoryRepository.confirmBooking(booking.getRoom().getId(),
                    booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());

            log.info("Booking confirmed for Booking ID {}", booking.getBookingId());

        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser() {

        return (User) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
