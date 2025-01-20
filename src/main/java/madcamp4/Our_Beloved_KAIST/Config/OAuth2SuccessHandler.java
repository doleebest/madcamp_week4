package madcamp4.Our_Beloved_KAIST.Config;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import madcamp4.Our_Beloved_KAIST.Domain.User;
import madcamp4.Our_Beloved_KAIST.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Authentication Success Handler Started");
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            log.debug("OAuth2User attributes: {}", oAuth2User.getAttributes());

            DatabaseReference usersRef = firebaseDatabase.getReference("users");
            log.info("Firebase Database Reference Created");

            Map<String, Object> userData = new HashMap<>();
            userData.put("email", oAuth2User.getAttribute("email"));
            userData.put("name", oAuth2User.getAttribute("name"));
            userData.put("lastLogin", System.currentTimeMillis());
            userData.put("url", "https://www.madcamp.store");

            String userId = oAuth2User.getAttribute("sub");
            log.info("Attempting to save user data for userId: {}", userId);

            ApiFuture<Void> future = usersRef.child(userId).setValueAsync(userData);

            future.addListener(() -> {
                try {
                    future.get(); // 작업 완료 대기
                    log.info("Successfully saved user data to Firebase for user: {}", userId);
                } catch (Exception e) {
                    log.error("Failed to save user data to Firebase: ", e);
                }
            }, Runnable::run);

            // User 엔티티 저장 또는 업데이트
            User user = userRepository.findById(userId)
                    .map(existingUser -> {
                        existingUser.updateLastLogin();
                        return existingUser;
                    })
                    .orElseGet(() -> User.builder()
                            .id(userId)
                            .email(oAuth2User.getAttribute("email"))
                            .name(oAuth2User.getAttribute("name"))
                            .build());

            userRepository.save(user);
            log.info("User saved to local database: {}", user.getId());

            response.sendRedirect("/api/auth/login/success");

        } catch (Exception e) {
            log.error("Error in OAuth2 success handler: ", e);
            throw e;
        }
    }
}