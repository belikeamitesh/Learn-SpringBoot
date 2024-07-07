package com.bookmyshow.BookingApp.Exceptions;

public class MovieAlreadyPresentWithSameNameAndLanguage extends RuntimeException {
    public MovieAlreadyPresentWithSameNameAndLanguage() {
        super("Movie is already exists with same name and language");
    }
}
