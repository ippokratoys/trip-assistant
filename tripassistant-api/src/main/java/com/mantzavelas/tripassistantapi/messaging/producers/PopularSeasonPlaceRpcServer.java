package com.mantzavelas.tripassistantapi.messaging.producers;

import com.mantzavelas.tripassistantapi.dtos.LocationDto;
import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.messaging.common.ConnectionFactoryInstance;
import com.mantzavelas.tripassistantapi.services.PlaceService;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class PopularSeasonPlaceRpcServer {

	private static final String QUEUE_NAME = "rpc.trip.popular.bySeason";

	private PlaceService placeService;
	private static final Object monitor = new Object();

	public PopularSeasonPlaceRpcServer() {
		placeService = BeanUtil.getBean(PlaceService.class);
	}

	public PopularSeasonPlaceRpcServer(PlaceService placeService) {
		this.placeService = placeService;
	}

	public ArrayList<PlaceDto> getPopularPlacesBySeason(String lat, String lon) {
		return new ArrayList<>(placeService.getPopularPlacesBySeason(lat, lon));
	}

	public void initRpcServer() {
		try (Connection connection = ConnectionFactoryInstance.INSTANCE.getConnection();
			 Channel channel = connection.createChannel()) {

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			channel.queuePurge(QUEUE_NAME);

			channel.basicQos(1);

			System.out.println(" [x] Awaiting Popular Season Place Requests");

			DeliverCallback deliverCallback = getDeliverCallback(channel);

			channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));

			while (true) {
				synchronized (monitor) {
					try {
						monitor.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DeliverCallback getDeliverCallback(Channel channel) {
		AtomicReference<ArrayList<PlaceDto>> response = new AtomicReference<>(new ArrayList<>());
		return (consumerTag, delivery) -> {
			AMQP.BasicProperties replyProps = new AMQP.BasicProperties
					.Builder()
					.correlationId(delivery.getProperties().getCorrelationId())
					.build();

			try {
				LocationDto location = (LocationDto) SerializationUtils.deserialize(delivery.getBody());

				System.out.println(" [X] Received message " + Arrays.toString(delivery.getBody()));

				if (location == null) {
					return;
				}

				response.set(getPopularPlacesBySeason(location.getLatitude(), location.getLongitude()));
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally {
				channel.basicPublish("", delivery.getProperties().getReplyTo()
						, replyProps, SerializationUtils.serialize(response.get()));
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				synchronized (monitor) {
					monitor.notifyAll();
				}
			}

		};
	}
}
