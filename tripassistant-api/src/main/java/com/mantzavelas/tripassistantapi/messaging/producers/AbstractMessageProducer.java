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

	public static final String HOST = PropertyUtil.getProperty("app.rabbitmq.host");
	public static final String V_HOST = PropertyUtil.getProperty("app.rabbitmq.vhost");
	public static final String USERNAME = PropertyUtil.getProperty("app.rabbitmq.user");
	public static final String PASSWORD = PropertyUtil.getProperty("app.rabbitmq.pass");

	private ConnectionFactory factory;
	protected Serializable message;

	public AbstractMessageProducer(Serializable message) {
		initConnFactory();

		this.message = message;

		connectAndSendMessage();
	}

	private void initConnFactory() {
		factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setVirtualHost(V_HOST);
		factory.setUsername(USERNAME);
		factory.setPassword(PASSWORD);
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
