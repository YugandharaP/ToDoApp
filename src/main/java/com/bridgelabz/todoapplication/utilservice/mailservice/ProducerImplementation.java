package com.bridgelabz.todoapplication.utilservice.mailservice;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.userservice.model.Email;

/**
 * @author yuga
 * @since 19/07/2018
 *        <p>
 * 		<b>To provide implementation for produce message interface.</b>
 *        </p>
 *
 */
@Service
public class ProducerImplementation implements IMailProducer {
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${todoapplication.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${todoapplication.rabbitmq.routingkey}")
	private String routingKey;

	/**@param to
	 * @param subject
	 * @param body
	 *<p><b> To produce message</b></p>
	 */
	@Override
	public void produceMessage(Email email) {
			amqpTemplate.convertAndSend(exchange, routingKey, email);
			System.out.println("message sent : with "+email);
	}

}
