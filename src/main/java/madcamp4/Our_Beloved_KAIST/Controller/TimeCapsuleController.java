package madcamp4.Our_Beloved_KAIST.Controller;


import madcamp4.Our_Beloved_KAIST.Dto.ItemRequest;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleDetails;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.Dto.TimeCapsuleResponse;
import madcamp4.Our_Beloved_KAIST.Service.TimeCapsuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timecapsules")
public class TimeCapsuleController {

    private final TimeCapsuleService timeCapsuleService;

    public TimeCapsuleController(TimeCapsuleService timeCapsuleService) {
        this.timeCapsuleService = timeCapsuleService;
    }

    @PostMapping("/create")
    public ResponseEntity<TimeCapsuleResponse> createTimeCapsule(@RequestBody TimeCapsuleRequest request) {
        TimeCapsuleResponse response = timeCapsuleService.createTimeCapsule(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<TimeCapsuleDetails> getTimeCapsuleByInviteCode(@PathVariable String inviteCode) {
        TimeCapsuleDetails details = timeCapsuleService.getTimeCapsuleByInviteCode(inviteCode);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/{capsuleId}/add-item")
    public ResponseEntity<String> addItemToCapsule(@PathVariable Long capsuleId, @RequestBody ItemRequest request) {
        timeCapsuleService.addItemToCapsule(capsuleId, request);
        return ResponseEntity.ok("Item added successfully");
    }
}
