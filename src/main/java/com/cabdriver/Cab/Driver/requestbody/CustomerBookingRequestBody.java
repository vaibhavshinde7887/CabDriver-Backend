package com.cabdriver.Cab.Driver.requestbody;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerBookingRequestBody {
   private String startingLocation;
   private String endingLocation;

}
