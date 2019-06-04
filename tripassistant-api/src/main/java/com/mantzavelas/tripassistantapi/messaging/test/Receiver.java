package com.mantzavelas.tripassistantapi.messaging.test;

import com.mantzavelas.tripassistantapi.messaging.producers.AbstractMessageProducer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Receiver {

	private static final String QUEUE_NAME = "helloworld";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(AbstractMessageProducer.HOST);
		factory.setVirtualHost(AbstractMessageProducer.V_HOST);
		factory.setUsername(AbstractMessageProducer.USERNAME);
		factory.setPassword(AbstractMessageProducer.PASSWORD);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback callback = (consumerTag, message) -> {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			System.out.println(" [x] Received '" + msg + "'");
		};

		channel.basicConsume(QUEUE_NAME, true, callback, consumerTag -> {});
	}
}
