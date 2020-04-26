package com.nagarro.ticketapp.server.repository;

import org.springframework.data.repository.CrudRepository;

import com.nagarro.ticketapp.server.model.User;

public interface UserRepo extends CrudRepository<User, String> {

}
