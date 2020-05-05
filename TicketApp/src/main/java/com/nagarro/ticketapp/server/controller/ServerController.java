package com.nagarro.ticketapp.server.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nagarro.ticketapp.server.exceptions.ApiRequestException;
import com.nagarro.ticketapp.server.model.User;
import com.nagarro.ticketapp.server.model.UserTicket;
import com.nagarro.ticketapp.server.repository.UserRepo;
import com.nagarro.ticketapp.server.repository.UserTicketRepo;
import com.nagarro.ticketapp.server.util.AuthenticationResponse;
import com.nagarro.ticketapp.server.util.JwtUtil;
import com.nagarro.ticketapp.server.util.RandomPasswordGenerator;
import com.nagarro.ticketapp.server.util.SendMail;
import com.nagarro.ticketapp.server.util.TicketResponseGenerator;
import com.nagarro.ticketapp.server.util.UserResponseGenerator;

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
	
	@GetMapping(value = "/addadmin", produces = { "application/json" })
	public Object init() {
		User admin = new User();
		admin.setAdd1("India");
		admin.setAdd2("India");
		admin.setAdmin(true);
		admin.setBU("Admin");
		admin.setCity("Gurgaon");
		admin.setCountry("India");
		admin.setEmail("admin@nagarro.com");
		admin.setfName("Admin");
		admin.setlName("Nagarro");
		admin.setPwd("Admin1@3");
		admin.setState("Harayana");
		admin.setTel("9876543210");
		admin.setTitle("Admin");
		admin.setZip(122006);
		return userRepo.save(admin);
	}
	
	@PostMapping(value = "/register", produces = { "application/json" })
	public Object register(@RequestBody Map<String, String> params) {
		try {
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
		SendMail.send(user.getEmail(), user.getPwd());
		return userRepo.save(user);
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@PostMapping(value = "/authenticate", produces = { "application/json" })
	public Object auth(@RequestBody Map<String, String> params) {
		User user = userRepo.findById(params.get("userName")).orElse(null);
		if (user == null)
			throw new ApiRequestException("No user found");
		else {
			final String jwt = jwtUtil.generateToken(user);
			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		}
	}
	
	@PostMapping(value = "/forgot", produces = { "application/json" })
	public Object forgot(@RequestBody Map<String, String> params) {
		try {
		String email = params.get("email");
		User user = userRepo.findById(email).orElse(null);
		if(user != null) {
			String pwd = user.getPwd();
			SendMail.send(email, pwd);
		}
		return ResponseEntity.ok("Mail sent");
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@PostMapping(value = "/addticket", produces = { "application/json" })
	public ResponseEntity<JsonObject> addTicket(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> params) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			UserTicket ticket = new UserTicket();
			ticket.setApprover(params.get("approver"));
			if (Integer.parseInt(params.get("cost_limit")) == 1)
				ticket.setLimit(params.get("limit"));
			else
				ticket.setLimit("");
			ticket.setBearer(Integer.parseInt(params.get("bearer")));
			ticket.setType(params.get("type"));
			ticket.setPriority(Integer.parseInt(params.get("priority")));
			ticket.setDestCity(params.get("destCity"));
			ticket.setSrcCity(params.get("srcCity"));
			ticket.setDestCountry(params.get("destCountry"));
			ticket.setSrcCountry(params.get("srcCountry"));
			String start_date = params.get("start_date");
			String temp[] = start_date.split("[//-]");
			start_date = temp[2] + "-" + temp[1] + "-" + temp[0];
			ticket.setStartDate(start_date);
			String end_date = params.get("end_date");
			String temp1[] = end_date.split("[//-]");
			end_date = temp1[2] + "-" + temp[1] + "-" + temp1[0];
			ticket.setEndDate(end_date);
			ticket.setProject(params.get("project"));
			ticket.setPassport(params.get("passport"));
			if (params.get("duration") != "")
				ticket.setDuration(Integer.parseInt(params.get("duration")));
			else
				ticket.setDuration(-1);
			ticket.setDetails(params.get("details"));
			ticket.setStatus(0);
			ticket.setProcessing(false);
			User user = userRepo.findById(id).orElse(null);
			user.setUserTickets(ticket);
			UserTicket cnfTicket = ticketRepo.save(ticket);
			JsonObject res = TicketResponseGenerator.generate(cnfTicket, id);
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@PostMapping(value = "/getticket", produces = { "application/json" })
	public JsonArray get(@RequestHeader Map<String, String> headers) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			User user = new User();
			user = userRepo.findById(id).orElse(null);
			List<UserTicket> tickets = user.getUserTickets();
			JsonArray res = new JsonArray();
			for(UserTicket ticket: tickets) {
				res.add(TicketResponseGenerator.generate(ticket, id));
			}
			return res;
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@PostMapping(value = "/getticketadmin", produces = { "application/json" })
	public JsonArray getall(@RequestHeader Map<String, String> headers) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			Iterable<User> users = userRepo.findAll();
			JsonArray res = new JsonArray();
			for(User user: users) {
				List<UserTicket> tickets = user.getUserTickets();
				for(UserTicket ticket: tickets) {
					res.add(TicketResponseGenerator.generateForAdmin(ticket, user));
				}
			}
			return res;
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@PostMapping(value = "/getusersadmin", produces = { "application/json" })
	public JsonArray getUsers(@RequestHeader Map<String, String> headers) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			Iterable<User> users = userRepo.findAll();
			JsonArray res = new JsonArray();
			for(User user: users) {
				if(user.isAdmin() == false)
					res.add(UserResponseGenerator.generateForAdmin(user));
			}
			//System.out.println(users);
			return res;
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@GetMapping(value = "/getuserdetail/{id}", produces = { "application/json" })
	public JsonObject userdetail(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") String email) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			User user = userRepo.findById(email).orElse(null);
			if(user == null)
				throw new ApiRequestException("No user found");
			JsonObject res = UserResponseGenerator.generateForAdmin(user);
			return res;
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@DeleteMapping(value = "/deleteuser/{id}", produces = { "application/json" })
	public Object deleteUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") String email){
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			User user = userRepo.findById(email).orElse(null);
			List<UserTicket> tickets = user.getUserTickets();
			for(UserTicket ticket: tickets)
				ticketRepo.deleteById(ticket.getId());
			userRepo.deleteById(email);
			return ResponseEntity.ok("User deleted");
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/updateticket", produces = { "application/json"} )
	public Object update(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> params) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			int ticket_id = Integer.parseInt(params.get("id"));
			int status = Integer.parseInt(params.get("status"));
			boolean isProcessing = params.containsKey("isProcessing");
			UserTicket ticket = ticketRepo.findById(ticket_id).orElse(null);
			ticket.setStatus(status);
			ticket.setProcessing(isProcessing);
			ticketRepo.save(ticket);
			return ResponseEntity.ok("Ticket details updated");
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
		
	}
	
	@PatchMapping(value = "/edituser/{id}", produces = { "application/json" })
	public Object editUser(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") String email, @RequestBody Map<String, String> params) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			User user = userRepo.findById(email).orElse(null);
			if(user == null)
				throw new ApiRequestException("No user found");
			user.setAdmin(false);
			user.setfName(params.get("firstName"));
			user.setlName(params.get("lastName"));
			user.setBU(params.get("BU"));
			user.setTitle(params.get("title"));
			user.setTel(params.get("tel"));
			user.setAdd1(params.get("address1"));
			user.setAdd2(params.get("address2"));
			user.setCity(params.get("city"));
			user.setState(params.get("state"));
			user.setCountry(params.get("country"));
			user.setZip(Long.parseLong(params.get("zip")));
			userRepo.save(user);
			return ResponseEntity.ok("User details updated");
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/editticket/{id}", produces = { "application/json" })
	public Object editTicket(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") String idx, @RequestBody Map<String, String> params) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String user = headers.get("id");
			if (!jwtUtil.validateToken(jwt, user))
				throw new ApiRequestException("Unauthorized user");
			int id = Integer.parseInt(idx);
			UserTicket ticket = new UserTicket();
			ticket = ticketRepo.findById(id).orElse(null);
			if(ticket == null)
				throw new ApiRequestException("No ticket found");
			ticket.setApprover(params.get("approver"));
			if (Integer.parseInt(params.get("cost_limit")) == 1)
				ticket.setLimit(params.get("limit"));
			else
				ticket.setLimit("");
			ticket.setBearer(Integer.parseInt(params.get("bearer")));
			ticket.setType(params.get("type"));
			ticket.setPriority(Integer.parseInt(params.get("priority")));
			ticket.setDestCity(params.get("destCity"));
			ticket.setSrcCity(params.get("srcCity"));
			ticket.setDestCountry(params.get("destCountry"));
			ticket.setSrcCountry(params.get("srcCountry"));
			String start_date = params.get("start_date");
			String temp[] = start_date.split("[//-]");
			start_date = temp[2] + "-" + temp[1] + "-" + temp[0];
			ticket.setStartDate(start_date);
			String end_date = params.get("end_date");
			String temp1[] = end_date.split("[//-]");
			end_date = temp1[2] + "-" + temp[1] + "-" + temp1[0];
			ticket.setEndDate(end_date);
			ticket.setProject(params.get("project"));
			ticket.setPassport(params.get("passport"));
			if (params.get("duration") != "")
				ticket.setDuration(Integer.parseInt(params.get("duration")));
			else
				ticket.setDuration(-1);
			ticket.setDetails(params.get("details"));
			ticket.setStatus(0);
			ticket.setProcessing(false);
			ticketRepo.save(ticket);
			return ResponseEntity.ok("Ticket Updated");
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@GetMapping(value = "/viewticket/{id}", produces = { "application/json" })
	public Object view(@RequestHeader Map<String, String> headers, @PathVariable(value = "id") String idx) {
		try {
			String jwt = headers.get("authorization").substring(7);
			String id = headers.get("id");
			if (!jwtUtil.validateToken(jwt, id))
				throw new ApiRequestException("Unauthorized user");
			int ticketId = Integer.parseInt(idx);
			UserTicket ticket = new UserTicket();
			ticket = ticketRepo.findById(ticketId).orElse(null);
			if(ticket == null)
				throw new ApiRequestException("No ticket found");
			JsonObject res = TicketResponseGenerator.generateTicket(ticket);
			return res;
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
}
