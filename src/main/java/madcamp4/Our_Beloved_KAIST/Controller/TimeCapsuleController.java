package madcamp4.Our_Beloved_KAIST.Controller;

import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.OpenableResponse;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Dto.*;
import madcamp4.Our_Beloved_KAIST.Service.TimeCapsuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/capsules")
@RequiredArgsConstructor
public class TimeCapsuleController {
    private final TimeCapsuleService timeCapsuleService;

    // 타임캡슐 생성
    @PostMapping
    public ResponseEntity<TimeCapsuleResponse> createCapsule(@RequestBody CreateCapsuleRequest request) {
        TimeCapsule capsule = timeCapsuleService.createCapsule(request);
        return ResponseEntity.ok(TimeCapsuleResponse.from(capsule));
    }

    // 구슬 생성
    @PostMapping("/{capsuleId}/memories")
    public ResponseEntity<MemoryResponse> createMemory(
            @PathVariable Long capsuleId,
            @RequestBody CreateMemoryRequest request) {
        Memory memory = timeCapsuleService.createMemory(capsuleId, request);
        return ResponseEntity.ok(MemoryResponse.from(memory));
    }

    // 캡슐 봉인
    @PostMapping("/{capsuleId}/seal")
    public ResponseEntity<TimeCapsuleResponse> sealCapsule(
            @PathVariable Long capsuleId,
            @RequestBody SealCapsuleRequest request) {
        TimeCapsule capsule = timeCapsuleService.sealCapsule(capsuleId, request.getOpenDate());
        return ResponseEntity.ok(TimeCapsuleResponse.from(capsule));
    }

    // 구슬 전체 조회
    @GetMapping("/{capsuleId}/memories")
    public ResponseEntity<List<MemoryResponse>> getAllMemories(@PathVariable Long capsuleId) {
        List<Memory> memories = timeCapsuleService.getAllMemories(capsuleId);
        return ResponseEntity.ok(memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList()));
    }

    // 특정 구슬 조회
    @GetMapping("/{capsuleId}/memories/{memoryId}")
    public ResponseEntity<MemoryResponse> getMemory(
            @PathVariable Long capsuleId,
            @PathVariable Long memoryId) {
        Memory memory = timeCapsuleService.getMemory(capsuleId, memoryId);
        return ResponseEntity.ok(MemoryResponse.from(memory));
    }

    // 캡슐 개봉 가능 여부 확인
    @GetMapping("/{capsuleId}/openable")
    public ResponseEntity<OpenableResponse> isOpenable(@PathVariable Long capsuleId) {
        boolean openable = timeCapsuleService.isOpenable(capsuleId);
        return ResponseEntity.ok(new OpenableResponse(openable));
    }
}
