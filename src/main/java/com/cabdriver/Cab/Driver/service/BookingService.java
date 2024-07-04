package com.cabdriver.Cab.Driver.service;

import com.cabdriver.Cab.Driver.exceptions.UserNotFound;
import com.cabdriver.Cab.Driver.models.Booking;
import com.cabdriver.Cab.Driver.models.Customer;
import com.cabdriver.Cab.Driver.repository.BookingRepository;
import com.cabdriver.Cab.Driver.responsebody.BookingResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    CustomerService customerService;

    @Autowired
    BookingRepository bookingRepository;
    public void handleCustomerRequest(String startingLocation, String endingLocation, int customerId){

//        it is the validate  customer are not
//        if customer id exist in our system so we will say customer is valid
//        else we will say customer is not valid

        Customer customer = customerService.getCustomerById(customerId);
        if(customer == null){
            throw  new UserNotFound(String.format("User with id %d does not exist", customer));
        }
//        Create Booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setStatus("Draft");
//        Draft is Something booking is Created but not accepted
        booking.setBillAmount(0);
        booking.setStartingLocation(startingLocation);
        booking.setEndingLocation(endingLocation);

        bookingRepository.save(booking);
    }
    public List<BookingResponseBody> getBooingByStatus(String state){
        List<Booking> bookingsList = bookingRepository.getBookingByStatus(state);

        List<BookingResponseBody> bookingResponseBodyList = new ArrayList<>();
        for(Booking booking : bookingsList){
            BookingResponseBody bookingResponseBody = new BookingResponseBody();
            bookingResponseBody.setBookingID(booking.getId());
            bookingResponseBody.setCustomerID(booking.getCustomer().getId());
            bookingResponseBody.setStartingLocation(booking.getStartingLocation());
            bookingResponseBody.setEndingLocation(booking.getEndingLocation());
            bookingResponseBody.setCustomerName(booking.getCustomer().getFirstName());
            bookingResponseBody.setBillingAmount(booking.getBillAmount());
            bookingResponseBody.setStatus(booking.getStatus());
            bookingResponseBodyList.add(bookingResponseBody);

        }
        return bookingResponseBodyList;
    }
}
