package com.nagarro.ticketapp.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.nagarro.ticketapp.server.model.UserTicket;

public interface UserTicketRepo extends CrudRepository<UserTicket, Integer> {
	//List<UserTicket> findByUsername(String username);
}
