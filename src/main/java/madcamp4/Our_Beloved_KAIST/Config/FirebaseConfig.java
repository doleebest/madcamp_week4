package madcamp4.Our_Beloved_KAIST.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {
    @Value("${firebase.credentials.path}")
    private String firebaseConfigPath;

    private FirebaseApp firebaseApp;

    @PostConstruct
    public void initialize() {
        try {
            log.info("Starting Firebase initialization...");
            log.info("Firebase config path: {}", firebaseConfigPath);

            InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://our-beloved-kaist-default-rtdb.firebaseio.com")
                    .build();

            log.info("Firebase options built successfully");

            if (FirebaseApp.getApps().isEmpty()) {
                firebaseApp = FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            } else {
                firebaseApp = FirebaseApp.getInstance();
                log.info("Existing Firebase application instance retrieved");
            }

            // 초기화 테스트
            FirebaseDatabase.getInstance(firebaseApp);
            log.info("Firebase Database instance successfully created");

        } catch (IOException e) {
            log.error("Firebase initialization error: ", e);
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        log.info("Creating FirebaseAuth bean");
        if (firebaseApp == null) {
            initialize();
        }
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        log.info("Creating FirebaseDatabase bean");
        if (firebaseApp == null) {
            initialize();
        }
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}