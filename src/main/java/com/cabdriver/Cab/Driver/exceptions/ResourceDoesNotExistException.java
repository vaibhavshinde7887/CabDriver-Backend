package com.cabdriver.Cab.Driver.exceptions;

public class ResourceDoesNotExistException extends RuntimeException{

    public ResourceDoesNotExistException(String mssg){
        super(mssg);
    }
}
