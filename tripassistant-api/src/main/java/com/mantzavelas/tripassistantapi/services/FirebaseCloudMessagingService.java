package com.mantzavelas.tripassistantapi.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mantzavelas.tripassistantapi.models.UserNotification;

import java.io.IOException;

public enum FirebaseCloudMessagingService {

	INSTANCE;

	public void initializeSdk() throws IOException {
		FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.getApplicationDefault())
// Use this when in need of firebase cloud db
//			.setDatabaseUrl("https://" + FIREBASE_DATABASE_NAME + ".firebaseio.com")
			.build();

		FirebaseApp.initializeApp(options);
	}

	public String sendSingleMessage(String deviceToken, UserNotification notification) throws FirebaseMessagingException {
		Message message = Message.builder()
			.setNotification(new Notification(notification.getTitle(), notification.getDetails()))
			.setToken(deviceToken)
			.build();

		String response = FirebaseMessaging.getInstance().send(message);
		System.out.println("Sent FCM message: " + response);

		return response;
	}
}
