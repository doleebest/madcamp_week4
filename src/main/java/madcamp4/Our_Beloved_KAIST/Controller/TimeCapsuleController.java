package madcamp4.Our_Beloved_KAIST.Controller;

import lombok.RequiredArgsConstructor;
import madcamp4.Our_Beloved_KAIST.Domain.GeoPoint;
import madcamp4.Our_Beloved_KAIST.Domain.Marble;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Dto.MarbleRequest;
import madcamp4.Our_Beloved_KAIST.Dto.NearbyRequest;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.Service.TimeCapsuleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/timecapsules")
@RequiredArgsConstructor
public class TimeCapsuleController {
    private final TimeCapsuleService timeCapsuleService;

    @PostMapping
    public ResponseEntity<TimeCapsule> createTimeCapsule(
            @RequestParam String name,
            @RequestParam String creatorName) {
        TimeCapsule capsule = timeCapsuleService.createTimeCapsule(name, creatorName);
        return ResponseEntity.ok(capsule);
    }

    @PostMapping("/{inviteCode}/join")
    public ResponseEntity<TimeCapsule> joinTimeCapsule(
            @PathVariable String inviteCode) {
        TimeCapsule capsule = timeCapsuleService.findByInviteCode(inviteCode);
        return ResponseEntity.ok(capsule);
    }

    @PostMapping("/{capsuleId}/marbles")
    public ResponseEntity<Marble> createMarble(
            @PathVariable String capsuleId,
            @RequestBody MarbleRequest marbleRequest) {

        // MarbleRequest에서 content와 mediaUrls를 가져옵니다.
        Marble marble = timeCapsuleService.addMarble(capsuleId, marbleRequest.getContent(), marbleRequest.getMediaUrls());

        return ResponseEntity.ok(marble);
    }



    @PostMapping("/{capsuleId}/seal")
    public ResponseEntity<Void> sealTimeCapsule(
            @PathVariable String capsuleId,
            @RequestBody TimeCapsuleRequest request) {
        timeCapsuleService.sealTimeCapsule(capsuleId, request.getLocation(), request.getOpenDate());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<TimeCapsule>> getNearbyTimeCapsules(@RequestBody NearbyRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        double radiusInKm = request.getRadiusInKm();

        List<TimeCapsule> nearbyCapsules = timeCapsuleService.findNearbyTimeCapsules(latitude, longitude, radiusInKm);
        return ResponseEntity.ok(nearbyCapsules);
    }

}