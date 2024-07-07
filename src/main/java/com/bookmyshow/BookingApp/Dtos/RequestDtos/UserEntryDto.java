package com.bookmyshow.BookingApp.Dtos.RequestDtos;

import com.bookmyshow.BookingApp.Enums.Gender;
import lombok.Data;

@Data
public class UserEntryDto {

    private String name;
    private Integer age;
    private String address;
    private String mobileNo;
    private String emailId;
    private Gender gender;
}