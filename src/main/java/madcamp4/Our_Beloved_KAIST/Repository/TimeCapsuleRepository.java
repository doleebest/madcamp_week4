package madcamp4.Our_Beloved_KAIST.Repository;

import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Long> {
    // 기본 CRUD 메서드들은 JpaRepository에서 제공

    // 특정 생성자의 모든 타임캡슐 조회
    List<TimeCapsule> findByCreatorOrderByCreatedAtDesc(String creator);

    // 개봉 가능한(개봉일이 지난) 타임캡슐 조회
    @Query("SELECT tc FROM TimeCapsule tc WHERE tc.sealed = true AND tc.openDate <= :now")
    List<TimeCapsule> findOpenableCapsules(@Param("now") LocalDateTime now);

    // 특정 날짜 범위 내의 타임캡슐 조회
    List<TimeCapsule> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime start,
            LocalDateTime end
    );
}

