package madcamp4.Our_Beloved_KAIST.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount = new FileInputStream("firebaseServiceAccountKey.json");
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
    public DatabaseReference memoriesReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference("memories");
    }
}