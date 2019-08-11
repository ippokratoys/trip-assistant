package com.mantzavelas.tripassistant.messaging.common;

import com.mantzavelas.tripassistant.utils.PropUtil;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionInstance {

    private static ConnectionFactory connectionFactory;
    private static Connection connection;

    private static ConnectionInstance instance;
    public static ConnectionInstance getInstance() {
        if (instance == null) {
            instance = new ConnectionInstance();
        }

        return instance;
    }

    private ConnectionInstance() { initConnectionFactory(); }

    private void initConnectionFactory() {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(PropUtil.getProperty("messaging.rabbitmq.host"));
		connectionFactory.setVirtualHost(PropUtil.getProperty("messaging.rabbitmq.virtual_host"));
		connectionFactory.setUsername(PropUtil.getProperty("messaging.rabbitmq.username"));
		connectionFactory.setPassword(PropUtil.getProperty("messaging.rabbitmq.password"));
    }

    public Connection getConnection() throws IOException, TimeoutException {
        if (connection == null) {
            connection = connectionFactory.newConnection();
        }

        return connection;
    }
}
