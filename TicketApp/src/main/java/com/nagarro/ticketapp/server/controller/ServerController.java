package com.nagarro.ticketapp.server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nagarro.ticketapp.server.exceptions.ApiRequestException;
import com.nagarro.ticketapp.server.model.User;
import com.nagarro.ticketapp.server.model.UserTicket;
import com.nagarro.ticketapp.server.repository.UserRepo;
import com.nagarro.ticketapp.server.repository.UserTicketRepo;
import com.nagarro.ticketapp.server.util.AuthenticationResponse;
import com.nagarro.ticketapp.server.util.JwtUtil;
import com.nagarro.ticketapp.server.util.RandomPasswordGenerator;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ServerController {

	@Autowired
	UserRepo userRepo;
	@Autowired
	UserTicketRepo ticketRepo;
	@Autowired
	JwtUtil jwtUtil;

	RestTemplate rest = new RestTemplate();

//	Validate jwt and id sample	
//	@PostMapping(value = "/demo", produces = "application/json")
//	public Object test(@RequestHeader Map<String, String> headers) {
//		System.out.println(headers);
//		return headers.get("authorization").substring(7);
//	}

//	@PostMapping(value = "/demo", produces = "application/json")
//	public Object test() {
//		SendCredentials.sendMail();
//		return null;
//	}

//	@PostMapping(value = "/jwt/decode", produces = "application/json")
//	public String decodeJwt(@RequestBody String token) {
//		String payload = token.split("\\.")[1];
//		return new String(Base64.decodeBase64(payload));
//	}

	@PostMapping(value = "/register", produces = { "application/json" })
	public Object register(@RequestBody Map<String, String> params) {
		User user = new User();
		if (userRepo.existsById(params.get("email")))
			throw new ApiRequestException("Email alredy exists");
		user.setAdmin(false);
		user.setfName(params.get("firstName"));
		user.setlName(params.get("lastName"));
		user.setBU(params.get("BU"));
		user.setTitle(params.get("title"));
		user.setEmail(params.get("email"));
		user.setTel(params.get("tel"));
		user.setAdd1(params.get("address1"));
		user.setAdd2(params.get("address2"));
		user.setCity(params.get("city"));
		user.setState(params.get("state"));
		user.setCountry(params.get("country"));
		user.setZip(Long.parseLong(params.get("zip")));
		user.setPwd(RandomPasswordGenerator.generatePassword(8));
		// SendCredentials.sendMail();
		return userRepo.save(user);
	}

	@PostMapping(value = "/authenticate", produces = { "application/json" })
	public Object auth(@RequestBody Map<String, String> params) {
		User user = userRepo.findById(params.get("userName")).orElse(null);
		if (user == null)
			throw new ApiRequestException("No user found");
		else {
			final String jwt = jwtUtil.generateToken(user);
			// System.out.println(jwt);
			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		}
	}

	@PostMapping(value = "/addTicket", produces = { "application/json" })
	public Object addTicket(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> params) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if(!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			UserTicket ticket = new UserTicket();
			ticket.setApprover(params.get("approver"));
			if (Integer.parseInt(params.get("cost_limit")) == 1)
				ticket.setLimit("limit");
			else
				ticket.setLimit("");
			ticket.setBearer(Integer.parseInt(params.get("bearer")));
			ticket.setType(params.get("type"));
			ticket.setPriority(Integer.parseInt(params.get("priority")));
			ticket.setDest(params.get("dest"));
			ticket.setSrc(params.get("src"));
			ticket.setStartDate(params.get("start_date"));
			ticket.setEndDate(params.get("end_date"));
			ticket.setProject(params.get("project"));
			ticket.setPassport(params.get("passport"));
			ticket.setDuration(Integer.parseInt(params.get("duration")));
			ticket.setDetails(params.get("details"));
			ticket.setStatus(0);
			return ticketRepo.save(ticket);
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
}
