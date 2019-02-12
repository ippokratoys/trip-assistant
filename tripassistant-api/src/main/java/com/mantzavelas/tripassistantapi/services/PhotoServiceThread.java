package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.utils.DateUtils;

public class PhotoServiceThread implements Runnable{

    private static final int PHOTO_THREAD_INTERVAL = DateUtils.hoursToMs(12);

    private PhotoService photoService = new PhotoService();

    @Override
    public void run() {
    	while (true) {
			System.out.println("Started " + this.getClass().getCanonicalName());
			try {
				System.out.println("Started fetching photos...");
				photoService.persistPhotosFromFlickr();
				System.out.println("Finished fetching photos.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Starting popular photos clustering");
			photoService.clusterPhotosBasedOnLocation();
			System.out.println("Finished.");

			try {
				Thread.sleep(PHOTO_THREAD_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }


}
