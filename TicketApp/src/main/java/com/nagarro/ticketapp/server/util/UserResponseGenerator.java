package com.nagarro.ticketapp.server.util;

import com.google.gson.JsonObject;
import com.nagarro.ticketapp.server.model.User;

public class UserResponseGenerator {

	public static JsonObject generateForAdmin(User u) {
		JsonObject user = new JsonObject();
		user.addProperty("fName", u.getfName());
		user.addProperty("lName", u.getlName());
		user.addProperty("email", u.getEmail());
		user.addProperty("BU", u.getBU());
		user.addProperty("address1", u.getAdd1());
		user.addProperty("address2", u.getAdd2());
		user.addProperty("city", u.getCity());
		user.addProperty("state", u.getState());
		user.addProperty("country", u.getCountry());
		user.addProperty("tel", u.getTel());
		user.addProperty("title", u.getTitle());
		user.addProperty("zip", u.getZip());
		return user;
	}

}
