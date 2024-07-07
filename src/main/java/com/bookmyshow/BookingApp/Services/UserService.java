package com.bookmyshow.BookingApp.Services;

import com.bookmyshow.BookingApp.Dtos.RequestDtos.UserEntryDto;
import com.bookmyshow.BookingApp.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.BookingApp.Exceptions.UserAlreadyExistsWithEmail;
import com.bookmyshow.BookingApp.Exceptions.UserDoesNotExists;
import com.bookmyshow.BookingApp.Models.Ticket;
import com.bookmyshow.BookingApp.Models.User;
import com.bookmyshow.BookingApp.Repository.UserRepository;
import com.bookmyshow.BookingApp.Transformers.TicketTransformer;
import com.bookmyshow.BookingApp.Transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public String addUser(UserEntryDto userEntryDto) throws UserAlreadyExistsWithEmail {
        if(userRepository.findByEmailId(userEntryDto.getEmailId()) != null) {
            throw new UserAlreadyExistsWithEmail();
        }
        User user = UserTransformer.userDtoToUser(userEntryDto);

        userRepository.save(user);
        return "User Saved Successfully";
    }

    public List<TicketResponseDto> allTickets(Integer userId) throws UserDoesNotExists {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()) {
            throw new UserDoesNotExists();
        }
        User user = userOpt.get();
        List<Ticket> ticketList = user.getTicketList();
        List<TicketResponseDto> ticketResponseDtos = new ArrayList<>();
        for(Ticket ticket : ticketList) {
            TicketResponseDto ticketResponseDto = TicketTransformer.returnTicket(ticket.getShow(), ticket);
            ticketResponseDtos.add(ticketResponseDto);
        }
        return ticketResponseDtos;
    }
}
