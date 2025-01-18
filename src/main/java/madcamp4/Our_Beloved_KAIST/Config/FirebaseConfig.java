package madcamp4.Our_Beloved_KAIST.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class FirebaseConfig {

    // 1) yaml의 "firebase.credentials.json" 프로퍼티 읽기
    @Value("${firebase.credentials.json}")
    private String firebaseCredentialsJson;

    // 2) yaml의 "firebase.database.url" 프로퍼티 읽기
    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @PostConstruct
    public void initialize() {
        try {
            log.info("==== Firebase 초기화 시작 ====");
            log.info("Firebase Database URL: {}", firebaseDatabaseUrl);
            log.info("Firebase Credentials (길이): {}",
                    (firebaseCredentialsJson == null ? "null" : firebaseCredentialsJson.length()));

            if (firebaseCredentialsJson == null || firebaseCredentialsJson.trim().isEmpty()) {
                log.error("Firebase credentials가 비어있습니다.");
                throw new IllegalStateException("firebase.credentials.json 프로퍼티(=FIREBASE_KEY)가 설정되지 않았습니다.");
            }

            // 문자열(JSON)을 InputStream으로 변환
            ByteArrayInputStream serviceAccount =
                    new ByteArrayInputStream(firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(firebaseDatabaseUrl)
                    .build();

            // 이미 FirebaseApp이 초기화돼 있지 않다면 새로 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                log.info("새 Firebase 앱 초기화");
                FirebaseApp.initializeApp(options);
            } else {
                log.info("기존 Firebase 앱 사용");
            }

            log.info("==== Firebase 초기화 완료 ====");

        } catch (Exception e) {
            log.error("Firebase 초기화 중 예외 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Firebase 초기화 실패: " + e.getMessage(), e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }
}

