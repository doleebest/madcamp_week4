package madcamp4.Our_Beloved_KAIST.Service;

import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class TimeCapsuleService {

    public TimeCapsule createTimeCapsule(TimeCapsuleCreateRequest request, User creator) {
        String inviteCode = generateInviteCode();
        TimeCapsule capsule = new TimeCapsule(creator, inviteCode, request.getOpenDate());
        return timeCapsuleRepository.save(capsule);
    }

    public Memory addMemory(Long capsuleId, MemoryCreateRequest request, User creator) {
        TimeCapsule capsule = findCapsuleById(capsuleId);
        validateCapsuleAccess(capsule, creator);

        Memory memory = new Memory(capsule, creator, request.getContent(),
                request.getMediaUrl(), request.getMediaType());
        return memoryRepository.save(memory);
    }

    public void scheduleNotification(TimeCapsule capsule) {
        // 알림 스케줄링 로직
        LocalDateTime notificationTime = capsule.getOpenDate().minusDays(1);
        // 스케줄러에 작업 등록
    }
}
