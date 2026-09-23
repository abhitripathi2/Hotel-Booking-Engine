package com.codingShuttle.projects.HotelManagement.App.service;

import com.codingShuttle.projects.HotelManagement.App.entity.Booking;
import com.codingShuttle.projects.HotelManagement.App.entity.User;
import com.codingShuttle.projects.HotelManagement.App.repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutServiceImpl implements CheckOutService {

    private final BookingRepository bookingRepository;


    @Override
    public String getCheckOutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating checkout session for booking: {}", booking.getBookingId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            Customer customer = Customer.create(customerParams);

            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("usd")
                                            .setUnitAmount(booking.getAmount().multiply(new java.math.BigDecimal(100)).longValue())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Booking for " + booking.getHotel().getName() + " - Room: " + booking.getRoom().getRoomType())
                                                            .setDescription("Booking ID: " + booking.getBookingId() + ", Check-in: " + booking.getCheckInDate() + ", Check-out: " + booking.getCheckOutDate())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
                    )
                    .build();

            Session session = Session.create(sessionParams);

            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking);
            log.info("Checkout session created successfully for booking: {}", booking.getBookingId());

            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }



    }
}
