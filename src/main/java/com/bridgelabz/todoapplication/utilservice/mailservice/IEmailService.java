package com.bridgelabz.todoapplication.utilservice.mailservice;

import javax.mail.MessagingException;

import com.bridgelabz.todoapplication.userservice.model.Email;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;

/**
 * @author yuga
 * @since 13/07/2018
 *<p></b>To Send Mail</b></p>
 */
public interface IEmailService {
	/**
	 * @param email
	 * @throws MessagingException
	 * <p><b> send mail to the intended user</b></p>
	 */
	public void sendEmail( Email email) throws ToDoExceptions, MessagingException;
}
