package com.bookmyshow.BookingApp.Exceptions;

public class MovieDoesNotExists extends RuntimeException{
    public MovieDoesNotExists() {
        super("Movie dose not Exists");
    }
}