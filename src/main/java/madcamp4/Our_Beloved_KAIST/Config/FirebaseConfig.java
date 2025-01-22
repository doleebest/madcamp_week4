package madcamp4.Our_Beloved_KAIST.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream(credentialsPath);  // 수정된 부분
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://our-beloved-kaist-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Bean
    public DatabaseReference timeCapsulesReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference("time_capsules");
    }

    @Bean
    public DatabaseReference markerReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference("ar_markers");
    }

    @Bean
    public DatabaseReference memoriesReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference("memories");
    }

//    @Bean
//    public FirebaseStorage firebaseStorage() {
//        return FirebaseStorage.getInstance();
//    }

    @Bean
    public StorageClient storageClient() {
        return StorageClient.getInstance();
    }
}