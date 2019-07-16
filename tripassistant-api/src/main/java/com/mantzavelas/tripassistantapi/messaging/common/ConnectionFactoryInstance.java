package com.mantzavelas.tripassistantapi.messaging.common;

import com.mantzavelas.tripassistantapi.messaging.producers.AbstractMessageProducer;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public enum ConnectionFactoryInstance {

	INSTANCE;

	private static ConnectionFactory connectionFactory;
	private static Connection connection;

	public ConnectionFactory getConnectionFactory() {
		if (connectionFactory == null) {
			initConnectionFactory();
		}

		return connectionFactory;
	}

	public Connection getConnection() throws IOException, TimeoutException {
		if (connection == null) {
			//init connection factory in case it's not already initialized
			getConnectionFactory();
			connection = connectionFactory.newConnection();
		}

		return connection;
	}

	private void initConnectionFactory() {
		connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(AbstractMessageProducer.HOST);
		connectionFactory.setVirtualHost(AbstractMessageProducer.V_HOST);
		connectionFactory.setUsername(AbstractMessageProducer.USERNAME);
		connectionFactory.setPassword(AbstractMessageProducer.PASSWORD);
	}
}
