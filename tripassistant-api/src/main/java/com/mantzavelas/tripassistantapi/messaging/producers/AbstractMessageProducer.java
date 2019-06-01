package com.mantzavelas.tripassistantapi.messaging.producers;

import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMessageProducer {

	private static final String HOST = PropertyUtil.getProperty("app.rabbitmq.host");

	private ConnectionFactory factory;
	protected Serializable message;

	public AbstractMessageProducer(Serializable message) {
		factory = new ConnectionFactory();
		factory.setHost(HOST);

		this.message = message;

		connectAndSendMessage();
	}

	protected abstract String getQueueName();

	protected boolean isDurable() {
		return false;
	}

	protected boolean isExclusive() {
		return false;
	}

	protected boolean shouldAutoDelete() {
		return false;
	}

	protected Map<String, Object> getArguments() {
		return null;
	}

	protected void connectAndSendMessage() {
		byte[] msg = SerializationUtils.serialize(message);

		try (Connection connection = factory.newConnection();
			 Channel channel = connection.createChannel()){

			channel.queueDeclare(getQueueName(), isDurable(), isExclusive(), shouldAutoDelete(), getArguments());
			channel.basicPublish("", getQueueName(), MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
			System.out.println("[x] Sent " + getQueueName() + " " +  message + Arrays.toString(msg));
		} catch (TimeoutException | IOException e) {
			System.out.println("Failed to send message.");
			e.printStackTrace();
		}

	}
}
