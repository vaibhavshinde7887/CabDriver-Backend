package com.cabdriver.Cab.Driver.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String mssg){
        super(mssg);
    }
}
