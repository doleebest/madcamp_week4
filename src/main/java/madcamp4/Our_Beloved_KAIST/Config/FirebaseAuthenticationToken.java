package madcamp4.Our_Beloved_KAIST.Config;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import java.util.Collections;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {
    private final FirebaseToken token;

    public FirebaseAuthenticationToken(FirebaseToken token) {
        super(Collections.emptyList());  // 권한 없이 생성
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token.getUid();
    }
}