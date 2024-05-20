package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.stereotype.Service;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseService {
	private final String bucketName = "gps-tracker-b8f9e.appspot.com";
    private final String directory = "unknown_faces/";


    public Map<String, Object> getCoordinates() {
    	DatabaseReference ref = FirebaseDatabase.getInstance("https://gps-tracker-b8f9e-default-rtdb.firebaseio.com/").getReference();
        final CountDownLatch latch = new CountDownLatch(1);
        final Map<String, Object>[] resultHolder = new Map[1];

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    resultHolder[0] = (Map<String, Object>) dataSnapshot.getValue();
                } else {
                    resultHolder[0] = Map.of("error", "No data found");
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                resultHolder[0] = Map.of("error", databaseError.getMessage());
                latch.countDown();
            }
        });

        try {
            if (!latch.await(10, TimeUnit.SECONDS)) { // Timeout of 10 seconds
                return Map.of("error", "Timeout waiting for Firebase response");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of("error", "Request interrupted");
        }

        return resultHolder[0];
    }


    
    public List<String> listImagesAsBase64() {
        List<String> base64Images = new ArrayList<>();
        Storage storage = StorageClient.getInstance().bucket(bucketName).getStorage();
        for (Blob blob : storage.list(bucketName, BlobListOption.prefix(directory)).iterateAll()) {
            if (blob.getName().endsWith(".jpg") || blob.getName().endsWith(".png")) {
                Blob imageBlob = storage.get(blob.getBlobId());
                byte[] imageBytes = imageBlob.getContent();
                String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);
                base64Images.add(base64Encoded);
            }
        }
        return base64Images;
    }
    
    
    }
