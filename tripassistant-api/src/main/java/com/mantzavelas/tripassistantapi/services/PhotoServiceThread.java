package com.mantzavelas.tripassistantapi.services;

public class PhotoServiceThread implements Runnable{

    private PhotoService photoService = new PhotoService();

    @Override
    public void run() {
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
    }


}
