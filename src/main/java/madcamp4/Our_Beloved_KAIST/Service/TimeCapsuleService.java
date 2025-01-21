package madcamp4.Our_Beloved_KAIST.Service;

import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Dto.CreateMemoryRequest;
import madcamp4.Our_Beloved_KAIST.Repository.MemoryRepository;
import madcamp4.Our_Beloved_KAIST.Repository.TimeCapsuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeCapsuleService {
    private final TimeCapsuleRepository capsuleRepository;
    private final MemoryRepository memoryRepository;

    @Transactional
    public TimeCapsule createCapsule(CreateCapsuleRequest request) {
        TimeCapsule capsule = new TimeCapsule();
        capsule.setName(request.getName());
        capsule.setCreator(request.getCreator());
        capsule.setCreatedAt(LocalDateTime.now());
        capsule.setSealed(false);
        return capsuleRepository.save(capsule);
    }

    @Transactional
    public Memory createMemory(Long capsuleId, CreateMemoryRequest request) {
        TimeCapsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Capsule not found"));

        if (capsule.isSealed()) {
            throw new IllegalStateException("Cannot add memories to sealed capsule");
        }

        Memory memory = new Memory();
        memory.setType(request.getType());
        memory.setContent(request.getContent());
        memory.setTimeCapsule(capsule);
        memory.setCreatedAt(LocalDateTime.now());

        return memoryRepository.save(memory);
    }

    @Transactional
    public TimeCapsule sealCapsule(Long capsuleId, LocalDateTime openDate) {
        TimeCapsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Capsule not found"));

        if (capsule.isSealed()) {
            throw new IllegalStateException("Capsule is already sealed");
        }

        capsule.setSealed(true);
        capsule.setOpenDate(openDate);
        return capsuleRepository.save(capsule);
    }

    @Transactional(readOnly = true)
    public List<Memory> getAllMemories(Long capsuleId) {
        TimeCapsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Capsule not found"));
        return memoryRepository.findByTimeCapsuleIdOrderByCreatedAtDesc(capsuleId);
    }

    @Transactional(readOnly = true)
    public Memory getMemory(Long capsuleId, Long memoryId) {
        return memoryRepository.findByIdAndTimeCapsuleId(memoryId, capsuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Memory not found"));
    }

    @Transactional(readOnly = true)
    public boolean isOpenable(Long capsuleId) {
        TimeCapsule capsule = capsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new ResourceNotFoundException("Capsule not found"));

        if (!capsule.isSealed()) {
            return false;
        }

        return LocalDateTime.now().isAfter(capsule.getOpenDate());
    }
}