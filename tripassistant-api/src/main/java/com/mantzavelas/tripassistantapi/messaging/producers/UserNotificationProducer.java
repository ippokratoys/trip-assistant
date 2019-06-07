package com.mantzavelas.tripassistantapi.messaging.producers;

import com.mantzavelas.tripassistantapi.models.UserNotification;

public class UserNotificationProducer extends AbstractMessageProducer {

	private static final String QUEUE_NAME = "trip.notification";

	public UserNotificationProducer(UserNotification notification) {
		super(notification);
	}

	@Override
	protected String getQueueName() {
		return QUEUE_NAME;
	}

}
