package madcamp4.Our_Beloved_KAIST.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final FirebaseAuth firebaseAuth;

    @Autowired  // 명시적으로 Autowired 사용
    public UserController(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @GetMapping("/login/success")
    public ResponseEntity<Map<String, String>> loginSuccess(
            @AuthenticationPrincipal OAuth2User principal) {

        try {
            // Create Firebase custom token
            String uid = principal.getAttribute("sub");
            String email = principal.getAttribute("email");

            // Create Firebase user if not exists
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setUid(uid)
                    .setEmail(email)
                    .setEmailVerified(true)
                    .setDisplayName(principal.getAttribute("name"));

            try {
                firebaseAuth.createUser(request);
            } catch (FirebaseAuthException e) {
                // User might already exist
                log.info("User already exists: " + uid);
            }

            // Create custom token
            String customToken = firebaseAuth.createCustomToken(uid);

            Map<String, String> response = new HashMap<>();
            response.put("firebaseToken", customToken);

            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            log.error("Firebase authentication error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/login/failure")
    public ResponseEntity<String> loginFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed");
    }
}