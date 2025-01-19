package madcamp4.Our_Beloved_KAIST.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    public User getUser(String uid) {
        // Firebase에서 사용자 정보를 가져오는 로직
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        return User.builder()
                .uid(userRecord.getUid())
                .email(userRecord.getEmail())
                .name(userRecord.getDisplayName())
                .build();
    }
}