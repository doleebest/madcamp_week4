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
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {
    @Value("${firebase.credentials.path}")
    private String firebaseConfigPath;

    private FirebaseApp firebaseApp;

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://our-beloved-kaist-default-rtdb.firebaseio.com") // Firebase Console에서 확인할 수 있는 DB URL
                    .build();

            // 이미 초기화된 앱이 있는지 확인
            if (FirebaseApp.getApps().isEmpty()) {
                firebaseApp = FirebaseApp.initializeApp(options);
            } else {
                firebaseApp = FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            log.error("Firebase initialization error", e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        // Firebase 앱이 초기화된 후에 FirebaseAuth 인스턴스 생성
        if (firebaseApp == null) {
            initialize();
        }
        return FirebaseAuth.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        if (firebaseApp == null) {
            initialize();
        }
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}