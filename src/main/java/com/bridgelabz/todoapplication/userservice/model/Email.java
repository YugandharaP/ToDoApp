package com.bridgelabz.todoapplication.userservice.model;

/**
 * @author yuga
 * @since 13/07/2018
 * <p><b>To provide setter and getter methods to deal with user email details</b></p>
 *
 */
public class Email {
	private String to;

	private String subject;

	private String body;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
