package com.mantzavelas.tripassistantapi.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mantzavelas.tripassistantapi.models.UserNotification;
import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public enum FirebaseCloudMessagingService {

	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseCloudMessagingService.class);

	private static final String TOKEN_LOCATION = PropertyUtil.getProperty("app.fcm.tokenLocation");

	public void initializeSdk() throws IOException {
		FileInputStream refreshToken = new FileInputStream(TOKEN_LOCATION);

		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(refreshToken))
// Use this when in need of firebase cloud db
//			.setDatabaseUrl("https://" + FIREBASE_DATABASE_NAME + ".firebaseio.com")
			.build();

		LOGGER.info("Initializing firebase app for project: " + options.getProjectId());

		FirebaseApp.initializeApp(options);
		LOGGER.info("Initialized firebase app with name: " + FirebaseApp.getInstance().getName());
	}

	public String sendSingleMessage(String deviceToken, UserNotification notification) throws FirebaseMessagingException {
		Message message = Message.builder()
			.setNotification(new Notification(notification.getTitle(), notification.getDetails()))
			.setToken(deviceToken)
			.build();

		String response = FirebaseMessaging.getInstance().send(message);
		LOGGER.info("Sent FCM message: " + response);

		return response;
	}
}
