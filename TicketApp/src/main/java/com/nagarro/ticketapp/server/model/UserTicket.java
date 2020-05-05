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
	private Integer id;
	
	@Column(name = "ticket_type")
	private String type;
	
	@Column(name = "priority")
	private Integer priority;
	
	@Column(name = "travel_country")
	private String destCountry;
	
	@Column(name = "travel_city")
	private String destCity;
	
	@Column(name = "origin_country")
	private String srcCountry;
	
	@Column(name = "origin_city")
	private String srcCity;
	
	@Column(name = "start_date")
	private String startDate;
	
	@Column(name = "end_date")
	private String endDate;
	
	@Column(name = "project_name")
	private String project;
	
	@Column(name = "passport")
	private String passport;
	
	@Column(name = "expense_borne_by")
	private Integer bearer;
	
	@Column(name = "travel_approver")
	private String approver;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "cost_limit")
	private String limit;
	
	@Column(name = "additional_details")
	private String details;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "isProcessing")
	private Boolean processing;
	
	@ManyToOne
	@JoinColumn(name = "username")
	private User user;

	public Boolean isProcessing() {
		return processing;
	}

	public void setProcessing(Boolean processing) {
		this.processing = processing;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getDestCity() {
		return destCity;
	}

	public void setDestCity(String destCity) {
		this.destCity = destCity;
	}

	public String getSrcCountry() {
		return srcCountry;
	}

	public void setSrcCountry(String srcCountry) {
		this.srcCountry = srcCountry;
	}

	public String getSrcCity() {
		return srcCity;
	}

	public void setSrcCity(String srcCity) {
		this.srcCity = srcCity;
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

	public Integer getBearer() {
		return bearer;
	}

	public void setBearer(Integer bearer) {
		this.bearer = bearer;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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
		return "UserTicket [id=" + id + ", type=" + type + ", priority=" + priority + ", destCountry=" + destCountry
				+ ", destCity=" + destCity + ", srcCountry=" + srcCountry + ", srcCity=" + srcCity + ", startDate="
				+ startDate + ", endDate=" + endDate + ", project=" + project + ", passport=" + passport + ", bearer="
				+ bearer + ", approver=" + approver + ", duration=" + duration + ", limit=" + limit + ", details="
				+ details + ", status=" + status + ", processing=" + processing + ", user=" + user + "]";
	}
	
	
	
}
