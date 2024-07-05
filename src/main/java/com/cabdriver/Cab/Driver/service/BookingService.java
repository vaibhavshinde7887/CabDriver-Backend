package com.cabdriver.Cab.Driver.service;

import com.cabdriver.Cab.Driver.exceptions.InvalidOperationException;
import com.cabdriver.Cab.Driver.exceptions.ResourceDoesNotExistException;
import com.cabdriver.Cab.Driver.exceptions.UserNotFound;
import com.cabdriver.Cab.Driver.models.AppUser;
import com.cabdriver.Cab.Driver.models.Booking;
import com.cabdriver.Cab.Driver.models.Customer;
import com.cabdriver.Cab.Driver.models.Driver;
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
    DriverService driverService;

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

    public String updateBooking(String operation,
                              String email,
                              Integer bookingId){

//         we need to identify is this a customer email or is this a driver email
              Customer customer = customerService.getCustomerByEmail(email);
              Driver driver = driverService.getDriverByEmail(email);
              String userType = "";
        Integer userId = -1;
              AppUser user    = null;
              if(customer != null){
                   userId = customer.getId();
                  userType = "CUSTOMER";
                  user = customer;

              }else if (driver != null){
                userId = driver.getId();
                  userType = "DRIVER";
                  user = driver;
              }else {
                  throw new UserNotFound(String.format("User with id %d dose not exist",userId));
              }

        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if(booking == null){

            throw new ResourceDoesNotExistException(String.format("Booking with id %d does not exist in system",
                    bookingId));
        }

              if(operation.equals("ACCEPT")){
                  if(userType.equals("CUSTOMER")){

                      throw new InvalidOperationException(String.format("Customer can not accept rides"));
                  }
              //we want driver to accept the ride


                  booking.setDriver(driver);
                  booking.setStatus("ACCEPTED");
                  booking.setBillAmount(100);
                  bookingRepository.save(booking);
                  return String.format(
                          "Driver with id %d accepted booking with id %d ",
                          userId,
                          bookingId
                  );
              } else if (operation.equals("CANCEL")) {
                  if(userType.equals("CUSTOMER")){
                      if(booking.getCustomer().getId() == userId){
                          booking.setStatus("CANCELED");
                          bookingRepository.save(booking);

                         return  String.format("Customer with id %d cancelled ride with booking id %d",
                                 userId,bookingId);

                        }else {
                          throw new InvalidOperationException(String.format(
                                  "Customer with %d is not allowed to cancel booking with id %d",
                                  userId,
                                  bookingId
                          ));
                      }


                  } else if (userType.equals("DRIVER")) {
                      if(booking.getDriver().getId() == userId){
                          booking.setStatus("CANCELLED");
                          bookingRepository.save(booking);
                          return String.format(
                                  "Driver with id %d cancelled booking with id %d",
                                  userId,
                                  bookingId
                          );
                      }else {
                          throw new InvalidOperationException(String.format(
                                  "Driver with %d is not allowed to cancel booking with id %d",
                                  userId,
                                  bookingId));
                      }
                  }

              }
              return "";

       }
   }
