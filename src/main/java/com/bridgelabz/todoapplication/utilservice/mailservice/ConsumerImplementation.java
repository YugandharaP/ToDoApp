package com.bridgelabz.todoapplication.utilservice.mailservice;

import javax.mail.MessagingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.userservice.model.Email;
import com.bridgelabz.todoapplication.utilservice.exceptions.ToDoExceptions;

/**
 * @author yuga
 * @since 19/07/2018
 * <p><b>To provide implementation for consumer logic to receive mail from producer</b></p>
 *
 */
@Service
public class ConsumerImplementation implements IMailConsumer{
	@Autowired
	IEmailService emailService;
	/**To receive mail from producer
	 * @throws ToDoExceptions 
	 * */
	
	@Override
	@RabbitListener(queues = "${todoapplication.rabbitmq.queue}")
	public void recievedMessage(Email email) throws MessagingException, ToDoExceptions {
			System.out.println("Recieved Message: " + email);
			emailService.sendEmail(email);
		}

}
