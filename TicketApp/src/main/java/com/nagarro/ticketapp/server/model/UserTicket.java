package com.nagarro.ticketapp.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "ticket_details")
public class UserTicket {
	@Id
	@GeneratedValue
	private int id;
	
	@Column(name = "ticket_type")
	private String type;
	
	@Column(name = "priority")
	private int priority;
	
	@Column(name = "travel_city")
	private String dest;
	
	@Column(name = "origin_city")
	private String src;
	
	@Column(name = "start_date")
	private String startDate;
	
	@Column(name = "end_date")
	private String endDate;
	
	@Column(name = "project_name")
	private String project;
	
	@Column(name = "passport")
	private String passport;
	
	@Column(name = "expense_borne_by")
	private int bearer;
	
	@Column(name = "travel_approver")
	private String approver;
	
	@Column(name = "duration")
	private int duration;
	
	@Column(name = "cost_limit")
	private String limit;
	
	@Column(name = "additional_details")
	private String details;
	
	@Column(name = "status")
	private int status;
	
	@ManyToOne
	@JoinColumn(name = "email")
	private User user;

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public int getBearer() {
		return bearer;
	}

	public void setBearer(int bearer) {
		this.bearer = bearer;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserTicket [id=" + id + ", type=" + type + ", priority=" + priority + ", dest=" + dest + ", src=" + src
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", project=" + project + ", passport="
				+ passport + ", bearer=" + bearer + ", approver=" + approver + ", duration=" + duration + ", limit="
				+ limit + ", details=" + details + ", status=" + status + ", user=" + user + "]";
	}
	
	
}
