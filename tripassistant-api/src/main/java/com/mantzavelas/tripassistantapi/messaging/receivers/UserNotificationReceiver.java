package com.mantzavelas.tripassistantapi.messaging.receivers;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.mantzavelas.tripassistantapi.models.Trip;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.models.UserNotification;
import com.mantzavelas.tripassistantapi.repositories.TripRepository;
import com.mantzavelas.tripassistantapi.repositories.UserNotificationRepository;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.services.FirebaseCloudMessagingService;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.EncryptUtil;
import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import com.mantzavelas.tripassistantapi.utils.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class UserNotificationReceiver {

	private static final String QUEUE_NAME = "trip.notification";
	private static final String HOST = PropertyUtil.getProperty("app.rabbitmq.host");

	private ConnectionFactory factory;
	private UserNotificationRepository repository = BeanUtil.getBean(UserNotificationRepository.class);
	private TripRepository tripRepository = BeanUtil.getBean(TripRepository.class);
	private UserRepository userRepository = BeanUtil.getBean(UserRepository.class);

	public UserNotificationReceiver() {
		factory = new ConnectionFactory();
		factory.setHost(HOST);

		initQueueConnection();
	}

	private void initQueueConnection() {
		try {
			final Connection connection = factory.newConnection();
			final Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println("[*] Waiting for trip.notification messages");

			channel.basicQos(1);

			channel.basicConsume(QUEUE_NAME, false, getDeliverCallback(channel), consumerTag -> {});
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	private DeliverCallback getDeliverCallback(Channel channel) {
		return (consumerTag, delivery) -> {
					UserNotification message = (UserNotification) SerializationUtils.deserialize(delivery.getBody());

					System.out.println("[x] Received trip.notification message with id::" + message.getId());
					try {
						sendNotification(message);
					} finally {
						System.out.println("[x] Done");
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					}
				};
	}

	private void sendNotification(UserNotification notification) {
		User user = notification.getUser();
		//TODO: Hack to avoid getting Lazy Initialization Exception
		user = userRepository.findById(user.getId()).orElse(null);

		if (user == null) {
			System.out.println("Failed to send notification. No user defined.");
			return;
		}

		if (user.getDeviceTokens().isEmpty()) {
			System.out.println("No device tokens provided. Aborting notification sending.");
			return;
		}

		user.getDeviceTokens().stream()
				.map(EncryptUtil::decrypt)
				.map(StringUtil::removeQuotes)
				.forEach(getNotificationConsumer(notification));

		Trip trip = notification.getTrip();

		trip.setLastNotified(new Date());
		notification.setDateSent(new Date());

		tripRepository.save(trip);
		repository.save(notification);
	}

	private Consumer<String> getNotificationConsumer(UserNotification notification) {
		return token -> {
			try {
				String response = FirebaseCloudMessagingService.INSTANCE.sendSingleMessage(StringUtil.removeQuotes(token), notification);
				System.out.println("Successfully sent message: " + response);
			} catch (FirebaseMessagingException e) {
				System.out.println("Error sending message");
				e.printStackTrace();
			}
		};
	}
}
