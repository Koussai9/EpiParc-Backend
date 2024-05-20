package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Service
public class FirebaseInitialization {

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/java/com/example/demo/Firebase Credentials.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Initialized Successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize Firebase");
        }
    }
}
