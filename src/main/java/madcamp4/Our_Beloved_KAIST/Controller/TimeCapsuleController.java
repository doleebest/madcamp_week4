package madcamp4.Our_Beloved_KAIST.Controller;

import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.dto.response.OpenableResponse;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Service.TimeCapsuleService;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateMemoryRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.SealCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.response.MemoryResponse;
import madcamp4.Our_Beloved_KAIST.dto.response.TimeCapsuleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    @Async
    public ResponseEntity<MemoryResponse> createMemory(
            @PathVariable String capsuleId,  // capsuleId를 String으로 변경
            @RequestBody CreateMemoryRequest request) {
        Memory memory = timeCapsuleService.createMemory(capsuleId, request);
        return ResponseEntity.ok(MemoryResponse.from(memory));
    }

    // 캡슐 봉인
    @PostMapping("/{capsuleId}/seal")
    public ResponseEntity<TimeCapsuleResponse> sealCapsule(
            @PathVariable String capsuleId,  // capsuleId를 String으로 변경
            @RequestBody SealCapsuleRequest request) {
        TimeCapsule capsule = timeCapsuleService.sealCapsule(capsuleId, request.getOpenDate());
        return ResponseEntity.ok(TimeCapsuleResponse.from(capsule));
    }

    // 구슬 전체 조회
    @GetMapping("/{capsuleId}/memories")
    public ResponseEntity<List<MemoryResponse>> getAllMemories(@PathVariable String capsuleId) {  // capsuleId를 String으로 변경
        List<Memory> memories = timeCapsuleService.getAllMemories(capsuleId);
        return ResponseEntity.ok(memories.stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList()));
    }

    // 특정 구슬 조회
    @GetMapping("/{capsuleId}/memories/{memoryId}")
    @Async
    public CompletableFuture<ResponseEntity<MemoryResponse>> getMemory(
            @PathVariable String capsuleId,
            @PathVariable String memoryId) {

        return timeCapsuleService.getMemory(capsuleId, memoryId)
                .thenApply(memory -> ResponseEntity.ok(MemoryResponse.from(memory)))
                .exceptionally(ex -> ResponseEntity.status(500).body(null)); // 예외 처리
    }

    // 캡슐 개봉 가능 여부 확인
    @GetMapping("/{capsuleId}/openable")
    public ResponseEntity<OpenableResponse> isOpenable(@PathVariable String capsuleId) {  // capsuleId를 String으로 변경
        boolean openable = timeCapsuleService.isOpenable(capsuleId);
        return ResponseEntity.ok(new OpenableResponse(openable));
    }
}

