package madcamp4.Our_Beloved_KAIST.Controller;

import madcamp4.Our_Beloved_KAIST.Dto.MemoryDto;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timecapsules")
public class TimeCapsuleController {

    @PostMapping
    public ResponseEntity<TimeCapsuleDto> createTimeCapsule(@RequestBody TimeCapsuleCreateRequest request) {
        // 타임캡슐 생성 로직
    }

    @PostMapping("/{capsuleId}/memories")
    public ResponseEntity<MemoryDto> addMemory(@PathVariable Long capsuleId,
                                               @RequestBody MemoryCreateRequest request) {
        // 구슬(메모리) 추가 로직
    }

    @PatchMapping("/{capsuleId}/location")
    public ResponseEntity<TimeCapsuleDto> saveLocation(@PathVariable Long capsuleId,
                                                       @RequestBody LocationRequest request) {
        // 위치 저장 로직
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<TimeCapsuleDto>> getNearbyTimeCapsules(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        // 근처 타임캡슐 조회 로직
    }
}
