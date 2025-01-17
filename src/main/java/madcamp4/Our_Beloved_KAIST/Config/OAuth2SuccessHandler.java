package madcamp4.Our_Beloved_KAIST.Config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", oAuth2User.getAttribute("email"));
        userData.put("name", oAuth2User.getAttribute("name"));
        userData.put("lastLogin", System.currentTimeMillis());

        String userId = oAuth2User.getAttribute("sub"); // Google의 고유 ID
        usersRef.child(userId).setValueAsync(userData);

        response.sendRedirect("/api/auth/login/success");
    }
}