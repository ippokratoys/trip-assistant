package com.mantzavelas.tripassistantapi.services;

public class PhotoServiceThread implements Runnable{

    private PhotoService photoService = new PhotoService();

    @Override
    public void run() {
        try {
            photoService.persistPhotosFromFlickr();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        photoService.clusterPhotosBasedOnLocation();
    }


}
