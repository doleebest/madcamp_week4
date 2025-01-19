package madcamp4.Our_Beloved_KAIST.Repository;

import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Long> {
    Optional<TimeCapsule> findByInviteCode(String inviteCode);
}
