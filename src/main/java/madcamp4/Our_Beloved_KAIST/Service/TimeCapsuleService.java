// TimeCapsuleService.java
package madcamp4.Our_Beloved_KAIST.Service;

import com.google.firebase.database.*;
import lombok.extern.slf4j.Slf4j;
import madcamp4.Our_Beloved_KAIST.Domain.GeoPoint;
import madcamp4.Our_Beloved_KAIST.Domain.Marble;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TimeCapsuleService {
    private final DatabaseReference database;

    public TimeCapsuleService() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    // 타임캡슐 생성
    public TimeCapsule createTimeCapsule(String name, String creatorName) {
        TimeCapsule capsule = new TimeCapsule();
        capsule.setName(name);
        capsule.setCreatorName(creatorName);
        capsule.setInviteCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        capsule.setCreatedAt(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        capsule.setSealed(false);
        capsule.setMarbles(new ArrayList<>());
        capsule.setMemberIds(new ArrayList<>());

        DatabaseReference capsuleRef = database.child("timeCapsules").push();
        capsule.setId(capsuleRef.getKey());

        try {
            CompletableFuture<Void> future = new CompletableFuture<>();
            capsuleRef.setValue(capsule, (error, ref) -> {
                if (error != null) {
                    future.completeExceptionally(error.toException());
                } else {
                    future.complete(null);
                }
            });
            future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create time capsule", e);
        }

        return capsule;
    }

    // 초대 코드로 타임캡슐 찾기
    public TimeCapsule findByInviteCode(String inviteCode) {
        CompletableFuture<TimeCapsule> future = new CompletableFuture<>();

        database.child("timeCapsules")
                .orderByChild("inviteCode")
                .equalTo(inviteCode)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            future.complete(null);
                            return;
                        }

                        for (DataSnapshot capsuleSnapshot : snapshot.getChildren()) {
                            TimeCapsule capsule = capsuleSnapshot.getValue(TimeCapsule.class);
                            future.complete(capsule);
                            return;
                        }
                        future.complete(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        try {
            TimeCapsule result = future.get();
            if (result == null) {
                throw new RuntimeException("Invalid invite code");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find time capsule", e);
        }
    }

    // 구슬 추가
    public Marble addMarble(String capsuleId, String content, List<String> mediaUrls) {
        Marble marble = new Marble();
        marble.setContent(content);
        marble.setCreatedAt(LocalDateTime.now());
        marble.setMediaUrls(mediaUrls);

        DatabaseReference capsuleRef = database.child("timeCapsules").child(capsuleId);
        DatabaseReference marblesRef = capsuleRef.child("marbles");
        DatabaseReference newMarbleRef = marblesRef.push();
        marble.setId(newMarbleRef.getKey());

        try {
            CompletableFuture<Void> future = new CompletableFuture<>();
            newMarbleRef.setValue(marble, (error, ref) -> {
                if (error != null) {
                    future.completeExceptionally(error.toException());
                } else {
                    future.complete(null);
                }
            });
            future.get();
            return marble;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add marble", e);
        }
    }

    // 타임캡슐 봉인 및 위치 저장
    public void sealTimeCapsule(String capsuleId, GeoPoint location, LocalDateTime openDate) {
        DatabaseReference capsuleRef = database.child("timeCapsules").child(capsuleId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("location", location);
        updates.put("openDate", openDate);
        updates.put("sealed", true);

        try {
            CompletableFuture<Void> future = new CompletableFuture<>();
            capsuleRef.updateChildren(updates, (error, ref) -> {
                if (error != null) {
                    future.completeExceptionally(error.toException());
                } else {
                    future.complete(null);
                }
            });
            future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to seal time capsule", e);
        }
    }

    // 근처 타임캡슐 찾기
    public List<TimeCapsule> findNearbyTimeCapsules(double latitude, double longitude, double radiusInKm) {
        CompletableFuture<List<TimeCapsule>> future = new CompletableFuture<>();

        database.child("timeCapsules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<TimeCapsule> nearbyCapsules = new ArrayList<>();
                for (DataSnapshot capsuleSnapshot : snapshot.getChildren()) {
                    TimeCapsule capsule = capsuleSnapshot.getValue(TimeCapsule.class);
                    if (capsule != null && capsule.getLocation() != null) {
                        double distance = calculateDistance(
                                latitude, longitude,
                                capsule.getLocation().getLatitude(),
                                capsule.getLocation().getLongitude()
                        );
                        if (distance <= radiusInKm) {
                            nearbyCapsules.add(capsule);
                        }
                    }
                }
                future.complete(nearbyCapsules);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find nearby capsules", e);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반경 (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
