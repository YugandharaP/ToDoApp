package com.bridgelabz.todoapplication.utilservice.mailservice;

import javax.mail.MessagingException;

import com.bridgelabz.todoapplication.userservice.model.Email;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;

/**
 * @author yuga
 * @since 19/07/2018
 * <p><b>It is the interface between producer and consumer  to receive mail</b></p>
 *
 */
public interface IMailConsumer {
	/**
	 * @param email
	 * <p><b>To recieve mail from producer</b></p>
	 * @throws MessagingException
	 * @throws ToDoExceptions 
	 */
	void recievedMessage(Email email) throws MessagingException, ToDoExceptions;
}
