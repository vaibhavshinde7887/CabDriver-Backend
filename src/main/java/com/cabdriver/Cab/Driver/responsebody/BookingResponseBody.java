package com.cabdriver.Cab.Driver.responsebody;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookingResponseBody {
    private int bookingID;
    private int customerID;
    private String customerName;
    private String startingLocation;
    private String endingLocation;
    private int billingAmount;
    private String status;
}
