package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.messaging.producers.UserNotificationProducer;
import com.mantzavelas.tripassistantapi.models.Trip;
import com.mantzavelas.tripassistantapi.models.UserNotification;
import com.mantzavelas.tripassistantapi.repositories.TripRepository;
import com.mantzavelas.tripassistantapi.repositories.UserNotificationRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

public class UserTripNotifierThread implements Runnable {

	private static final int NOTIFIER_INTERVAL_MS = DateUtils.hoursToMs(12);

	private TripRepository tripRepository = BeanUtil.getBean(TripRepository.class);
	private UserNotificationRepository notificationRepository = BeanUtil.getBean(UserNotificationRepository.class);

	@Override
	public void run() {
		try {
			FirebaseCloudMessagingService.INSTANCE.initializeSdk();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			Slice<Trip> tripSlice = tripRepository.findByStatus(Trip.Status.UPCOMING, PageRequest.of(0, 10));

			do {
				tripSlice.get()
						 .filter(trip -> Trip.Status.UPCOMING.equals(trip.getStatus()))
						 .filter(this::isNotifiedBeforeYesterdayOrNull)
						 .map(tripToNotificationMapper())
						 .forEach( notification -> {
						 	notificationRepository.save(notification);
							new UserNotificationProducer(notification);
						 });

				tripSlice = tripRepository.findByStatus(Trip.Status.UPCOMING, tripSlice.nextPageable());
			} while (tripSlice.hasNext());

			try {
				Thread.sleep(NOTIFIER_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isNotifiedBeforeYesterdayOrNull(Trip trip) {
		Date yesterday = DateUtils.addDaysToDate(new Date(), -1);

		return trip.getLastNotified() == null || yesterday.after(trip.getLastNotified());
	}

	private Function<Trip, UserNotification> tripToNotificationMapper() {
		return trip -> {
			UserNotification notification = new UserNotification();
			notification.setTrip(trip);
			notification.setUser(trip.getUser());
			notification.setTitle("Exciting news! Your trip " + trip.getTitle() + "is approaching");
			notification.setDetails(trip.getTitle() + " is scheduled for " + trip.getScheduledFor() + " . Get ready!");

			return notification;
		};
	}

}
