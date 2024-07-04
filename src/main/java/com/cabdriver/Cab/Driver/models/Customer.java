package com.cabdriver.Cab.Driver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
//    @Entity annotation will convert this Customer model in our application code
//    Customer Table in our Database
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

//    @Id annotations used for uniQui key
    private Integer id;
    private Integer age;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private Long phoneNumber;
    private String address;
    @JsonIgnore
    @OneToMany(mappedBy = "customer")
  private List<Booking> bookings;

}
