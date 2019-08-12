package com.mantzavelas.tripassistant.messaging;

import android.util.Log;

import com.mantzavelas.tripassistant.messaging.common.ConnectionInstance;
import com.mantzavelas.tripassistant.restservices.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.dtos.LocationDto;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class PopularSeasonPlaceRequest implements AutoCloseable{

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc.trip.popular.bySeason";

    public PopularSeasonPlaceRequest() throws IOException, TimeoutException {
        connection = ConnectionInstance.getInstance().getConnection();
        channel = connection.createChannel();
    }

    public List<PlaceDto> getPlaces(String lat, String lon) throws IOException, InterruptedException {
        LocationDto locationDto = new LocationDto(lat, lon);

        final String correlationId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, SerializationUtils.serialize(locationDto));

        final BlockingQueue<List<PlaceDto>> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
                response.offer(new ArrayList<>(SerializationUtils.deserialize(delivery.getBody())));
                Log.d("[" + PopularSeasonPlaceRequest.class + "]", "Received a response from queue " + requestQueueName + "\n" + delivery.getBody());
            }
        }, consumerTag -> {
        });

        List<PlaceDto> result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }


}
