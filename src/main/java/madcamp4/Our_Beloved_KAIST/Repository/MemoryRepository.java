package madcamp4.Our_Beloved_KAIST.Repository;

import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.management.MemoryType;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    // 특정 캡슐의 모든 구슬 조회
    List<Memory> findByTimeCapsuleIdOrderByCreatedAtDesc(Long capsuleId);

    // 특정 캡슐의 특정 구슬 조회
    Optional<Memory> findByIdAndTimeCapsuleId(Long id, Long capsuleId);

    // 특정 타입의 구슬만 조회
    List<Memory> findByTimeCapsuleIdAndTypeOrderByCreatedAtDesc(
            Long capsuleId,
            MemoryType type
    );

    // 특정 캡슐의 구슬 개수 조회
    @Query("SELECT COUNT(m) FROM Memory m WHERE m.timeCapsule.id = :capsuleId")
    long countByCapsuleId(@Param("capsuleId") Long capsuleId);
}