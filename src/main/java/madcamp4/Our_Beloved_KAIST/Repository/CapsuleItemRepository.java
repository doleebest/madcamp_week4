package madcamp4.Our_Beloved_KAIST.Repository;

import madcamp4.Our_Beloved_KAIST.Domain.CapsuleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapsuleItemRepository extends JpaRepository<CapsuleItem, Long> {
    List<CapsuleItem> findByCapsuleId(Long capsuleId);
}
