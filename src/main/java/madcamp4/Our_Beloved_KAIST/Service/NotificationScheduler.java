package madcamp4.Our_Beloved_KAIST.Service;

import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationScheduler {

    @Scheduled(fixedRate = 60000) // 1분마다 체크
    public void checkAndSendNotifications() {
        List<TimeCapsule> capsulesToNotify = timeCapsuleRepository
                .findUpcomingCapsules(LocalDateTime.now().plusDays(1));

        for (TimeCapsule capsule : capsulesToNotify) {
            sendNotification(capsule);
        }
    }

    private void sendNotification(TimeCapsule capsule) {
        // Firebase Cloud Messaging을 통한 알림 발송
    }
}
