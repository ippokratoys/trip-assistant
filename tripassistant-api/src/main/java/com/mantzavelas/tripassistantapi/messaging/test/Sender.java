package com.mantzavelas.tripassistantapi.messaging.test;

import com.mantzavelas.tripassistantapi.messaging.producers.AbstractMessageProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {

	private static final String QUEUE_NAME = "helloworld";

	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(AbstractMessageProducer.HOST);
		factory.setVirtualHost(AbstractMessageProducer.V_HOST);
		factory.setUsername(AbstractMessageProducer.USERNAME);
		factory.setPassword(AbstractMessageProducer.PASSWORD);

		try (Connection connection = factory.newConnection();
			Channel channel = connection.createChannel()) {
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Test Message!!!";
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println("[x] Sent '" + message + "'");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
