package com.bookmyshow.BookingApp.Services;
import com.bookmyshow.BookingApp.Dtos.RequestDtos.TicketEntryDto;
import com.bookmyshow.BookingApp.Dtos.ResponseDtos.TicketResponseDto;
import com.bookmyshow.BookingApp.Exceptions.RequestedSeatAreNotAvailable;
import com.bookmyshow.BookingApp.Exceptions.ShowDoesNotExists;
import com.bookmyshow.BookingApp.Exceptions.UserDoesNotExists;
import com.bookmyshow.BookingApp.Models.Show;
import com.bookmyshow.BookingApp.Models.ShowSeat;
import com.bookmyshow.BookingApp.Models.Ticket;
import com.bookmyshow.BookingApp.Models.User;
import com.bookmyshow.BookingApp.Repository.*;
import com.bookmyshow.BookingApp.Transformers.TicketTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public TicketResponseDto ticketBooking(TicketEntryDto ticketEntryDto) throws RequestedSeatAreNotAvailable, UserDoesNotExists, ShowDoesNotExists {
        // check user present
        Optional<Show> showOpt = showRepository.findById(ticketEntryDto.getShowId());
        if(showOpt.isEmpty()) {
            throw new ShowDoesNotExists();
        }

        //check show present
        Optional<User> userOpt = userRepository.findById(ticketEntryDto.getUserId());
        if(userOpt.isEmpty()) {
            throw new UserDoesNotExists();
        }

        User user = userOpt.get();
        Show show = showOpt.get();

        //check requested seat available
        Boolean isSeatAvailable = isSeatAvailable(show.getShowSeatList(), ticketEntryDto.getRequestSeats());
        if(!isSeatAvailable) {
            throw new RequestedSeatAreNotAvailable();
        }

        // count price
        Integer getPriceAndAssignSeats = getPriceAndAssignSeats(show.getShowSeatList(),ticketEntryDto.getRequestSeats());

        // change list to string
        String seats = listToString(ticketEntryDto.getRequestSeats());

        // create ticket entity and set all attribute
        Ticket ticket = new Ticket();
        ticket.setTotalTicketsPrice(getPriceAndAssignSeats);
        ticket.setBookedSeats(seats);

        // setting foreign key variables
        ticket.setUser(user);
        ticket.setShow(show);

        ticket = ticketRepository.save(ticket);

        user.getTicketList().add(ticket);
        show.getTicketList().add(ticket);
        userRepository.save(user);
        showRepository.save(show);

        // write mail and send to user Id
        sendMailToUser(user, show,seats);

        // build Ticket Response Dto
        return TicketTransformer.returnTicket(show, ticket);
    }

    private void sendMailToUser(User user, Show show, String seats) {
        String body = "Dear"+user.getName()+",\n\nI hope this email finds you well. \n" +
                "I am writing to inform you that your ticket has been successfully booked. \n" +
                "We are pleased to confirm that your preferred date and time and more details have been secured.\n \n" +
                "Ticket Details:\n\n" +
                "Booked seat No's: "+seats+"\n" +
                "Movie Name: "+show.getMovie().getMovieName()+"\n" +
                "Date: "+show.getDate()+"\n" +
                "Time: "+show.getTime()+"\n" +
                "Location: "+show.getTheater().getAddress()+"\n\n"+
                "Enjoy the show !!";

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setText(body);
//        message.setFrom("amiteshbelike@gmail.com");
//        message.setTo(user.getEmailId());
//        message.setSubject("Ticket Successfully Booked!");
//        mailSender.send(message);
        System.out.println("Mail Sent Successfully with mail content - "+body);
    }

    private Boolean isSeatAvailable(List<ShowSeat> showSeatList, List<String> requestSeats) {
        for(ShowSeat showSeat : showSeatList) {
            String seatNo = showSeat.getSeatNo();
            if(requestSeats.contains(seatNo)) {
                if(!showSeat.getIsAvailable()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Integer getPriceAndAssignSeats(List<ShowSeat> showSeatList, List<String> requestSeats) {
        Integer totalAmount = 0;
        for(ShowSeat showSeat : showSeatList) {
            if(requestSeats.contains(showSeat.getSeatNo())) {
                totalAmount += showSeat.getPrice();
                showSeat.setIsAvailable(Boolean.FALSE);
            }
        }
        return totalAmount;
    }

    private String listToString(List<String> requestSeats) {
        StringBuilder sb = new StringBuilder();
        for(String s : requestSeats) {
            sb.append(s).append(",");
        }
        return sb.toString();
    }
}
