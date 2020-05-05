package com.nagarro.ticketapp.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_details")
public class User {
	@Id
	private String email;
	@Column(name = "password")
	private String pwd;
	@Column(name = "First_Name")
	private String fName;
	@Column(name = "Last_Name")
	private String lName;
	@Column(name = "Business_Unit")
	private String BU;
	@Column(name = "Title")
	private String title;
	@Column(name = "Telephone")
	private String tel;
	@Column(name = "Address1")
	private String add1;
	@Column(name = "Address2")
	private String add2;
	@Column(name = "City")
	private String city;
	@Column(name = "State")
	private String state;
	@Column(name = "Country")
	private String country;
	@Column(name = "Zip")
	private long zip;
	@Column(name = "IsAdmin")
	private boolean admin;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<UserTicket> UserTickets = new ArrayList<UserTicket>();

	public List<UserTicket> getUserTickets() {
		return UserTickets;
	}

	public void setUserTickets(UserTicket ticket) {
		if(UserTickets == null)
			UserTickets = new ArrayList<UserTicket>();
		ticket.setUser(this);
		UserTickets.add(ticket);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getBU() {
		return BU;
	}

	public void setBU(String bU) {
		BU = bU;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAdd1() {
		return add1;
	}

	public void setAdd1(String add1) {
		this.add1 = add1;
	}

	public String getAdd2() {
		return add2;
	}

	public void setAdd2(String add2) {
		this.add2 = add2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getZip() {
		return zip;
	}

	public void setZip(long zip) {
		this.zip = zip;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "Login [email=" + email + ", pwd=" + pwd + ", fName=" + fName + ", lName=" + lName + ", BU=" + BU
				+ ", title=" + title + ", tel=" + tel + ", add1=" + add1 + ", add2=" + add2 + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", zip=" + zip + ", IsAdmin=" + admin + "]";
	}
}
