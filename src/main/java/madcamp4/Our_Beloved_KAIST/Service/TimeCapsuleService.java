package madcamp4.Our_Beloved_KAIST.Service;

import madcamp4.Our_Beloved_KAIST.Domain.CapsuleItem;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Dto.ItemRequest;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleDetails;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleResponse;
import madcamp4.Our_Beloved_KAIST.Repository.CapsuleItemRepository;
import madcamp4.Our_Beloved_KAIST.Repository.TimeCapsuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TimeCapsuleService {

    private final TimeCapsuleRepository timeCapsuleRepository;
    private final CapsuleItemRepository capsuleItemRepository;

    public TimeCapsuleService(TimeCapsuleRepository timeCapsuleRepository, CapsuleItemRepository capsuleItemRepository) {
        this.timeCapsuleRepository = timeCapsuleRepository;
        this.capsuleItemRepository = capsuleItemRepository;
    }

    public TimeCapsuleResponse createTimeCapsule(TimeCapsuleRequest request) {
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);

        TimeCapsule timeCapsule = new TimeCapsule();
        timeCapsule.setName(request.getName());
        timeCapsule.setOwnerId(request.getOwnerId());
        timeCapsule.setInviteCode(inviteCode);
        timeCapsule.setOpenDate(request.getOpenDate());
        timeCapsule.setLocation(request.getLocation());
        timeCapsule.setCreatedAt(LocalDateTime.now());

        timeCapsuleRepository.save(timeCapsule);
        return new TimeCapsuleResponse(timeCapsule.getId(), inviteCode);
    }

    public TimeCapsuleDetails getTimeCapsuleByInviteCode(String inviteCode) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        return new TimeCapsuleDetails(
                timeCapsule.getId(),
                timeCapsule.getName(),
                timeCapsule.getOpenDate()
        );
    }

    public void addItemToCapsule(Long capsuleId, ItemRequest request) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new RuntimeException("Time capsule not found"));

        CapsuleItem item = new CapsuleItem();
        item.setCapsuleId(capsuleId);
        item.setType(request.getType());
        item.setContent(request.getContent());
        item.setCreatedAt(LocalDateTime.now());

        capsuleItemRepository.save(item);
    }
}

