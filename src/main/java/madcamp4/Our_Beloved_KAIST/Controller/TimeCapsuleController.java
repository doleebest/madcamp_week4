package madcamp4.Our_Beloved_KAIST.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.ARMarker;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.MemoryType;
import madcamp4.Our_Beloved_KAIST.Exception.ResourceNotFoundException;
import madcamp4.Our_Beloved_KAIST.dto.request.ARMarkerRequest;
import madcamp4.Our_Beloved_KAIST.dto.response.*;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Service.TimeCapsuleService;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateMemoryRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.SealCapsuleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{capsuleId}/memories")
    public ResponseEntity<MemoryResponse> createMemory(
            @PathVariable String capsuleId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("type") MemoryType type,
            @RequestParam(value = "content", required = false) String content) {
        try {
            CreateMemoryRequest request = new CreateMemoryRequest();
            request.setType(type);

            if (file != null && (type == MemoryType.IMAGE || type == MemoryType.VIDEO)) {
                // 파일이 있고 타입이 IMAGE나 VIDEO인 경우 파일 처리
                String fileUrl = timeCapsuleService.uploadFile(capsuleId, file, type);
                request.setContent(fileUrl);
            } else {
                // TEXT 타입인 경우 content 사용
                request.setContent(content);
            }

            Memory memory = timeCapsuleService.createMemory(capsuleId, request);
            return ResponseEntity.ok(MemoryResponse.from(memory));
        } catch (Exception e) {
            System.err.println("Error creating memory: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<List<MemoryResponse>> getAllMemories(@PathVariable String capsuleId) {
        try {
            System.out.println("Receiving request to get all memories for capsule: " + capsuleId);
            List<Memory> memories = timeCapsuleService.getAllMemories(capsuleId);
            List<MemoryResponse> responses = memories.stream()
                    .map(MemoryResponse::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("Error getting memories: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 구슬 조회
//    @GetMapping("/{capsuleId}/memories/{memoryId}")
//    @Async
//    public CompletableFuture<ResponseEntity<MemoryResponse>> getMemory(
//            @PathVariable String capsuleId,
//            @PathVariable String memoryId) {
//
//        return timeCapsuleService.getMemory(capsuleId, memoryId)
//                .thenApply(memory -> ResponseEntity.ok(MemoryResponse.from(memory)))
//                .exceptionally(ex -> ResponseEntity.status(500).body(null)); // 예외 처리
//    }

    @GetMapping("/{capsuleId}/memories/{memoryId}")
    public ResponseEntity<MemoryResponse> getMemory(@PathVariable String capsuleId, @PathVariable String memoryId) {
        try {
            Memory memory = timeCapsuleService.getMemory(capsuleId, memoryId);
            return ResponseEntity.ok(MemoryResponse.from(memory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 캡슐 개봉 가능 여부 확인
    @GetMapping("/{capsuleId}/openable")
    public ResponseEntity<OpenableResponse> isOpenable(@PathVariable String capsuleId) {
        try {
            System.out.println("Receiving request to check if capsule is openable: " + capsuleId);
            boolean openable = timeCapsuleService.isOpenable(capsuleId);
            return ResponseEntity.ok(new OpenableResponse(openable));
        } catch (Exception e) {
            System.err.println("Error checking capsule openable status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 근처 캡슐 조회
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyCapsuleResponse>> findNearbyCapsules(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius) {
        try {
            List<NearbyCapsuleResponse> nearbyCapsules =
                    timeCapsuleService.findNearbyCapsules(lat, lng, radius);
            return ResponseEntity.ok(nearbyCapsules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // AR 마커 저장
    @PostMapping("/{capsuleId}/ar-marker")
    public ResponseEntity<ARMarkerResponse> saveARMarker(
            @PathVariable String capsuleId,
            @RequestBody ARMarkerRequest request) {
        try {
            ARMarker marker = timeCapsuleService.saveARMarker(capsuleId, request);
            return ResponseEntity.ok(ARMarkerResponse.from(marker));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // AR 마커 조회
    @GetMapping("/{capsuleId}/ar-marker")
    public ResponseEntity<ARMarkerResponse> getARMarker(@PathVariable String capsuleId) {
        try {
            ARMarker marker = timeCapsuleService.getARMarker(capsuleId);
            return ResponseEntity.ok(ARMarkerResponse.from(marker));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

