package madcamp4.Our_Beloved_KAIST.Service;

import com.google.firebase.database.*;
import madcamp4.Our_Beloved_KAIST.Domain.ARMarker;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.MemoryType;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Exception.ResourceNotFoundException;
import madcamp4.Our_Beloved_KAIST.dto.request.ARMarkerRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateMemoryRequest;
import madcamp4.Our_Beloved_KAIST.dto.response.NearbyCapsuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

@Service
public class TimeCapsuleService {
    private final DatabaseReference capsuleReference;
    private final DatabaseReference memoryReference;
    private final DatabaseReference markerReference;

    @Autowired
    public TimeCapsuleService(
            @Qualifier("timeCapsulesReference") DatabaseReference capsuleReference,
            @Qualifier("memoriesReference") DatabaseReference memoryReference,
            @Qualifier("markerReference") DatabaseReference markerReference) {
        this.capsuleReference = capsuleReference;
        this.memoryReference = memoryReference;
        this.markerReference = markerReference;
    }
    public TimeCapsule createCapsule(CreateCapsuleRequest request) {
        String capsuleId = UUID.randomUUID().toString();
        TimeCapsule capsule = new TimeCapsule();
        capsule.setId(capsuleId);
        capsule.setName(request.getName());
        capsule.setCreator(request.getCreator());
        capsule.setCreatedAt(LocalDateTime.now());
        capsule.setSealed(false);

        Map<String, Object> capsuleValues = new HashMap<>();
        capsuleValues.put("id", capsule.getId());
        capsuleValues.put("name", capsule.getName());
        capsuleValues.put("creator", capsule.getCreator());
        capsuleValues.put("createdAt", capsule.getCreatedAt().toString());
        capsuleValues.put("sealed", capsule.isSealed());

        capsuleReference.child(capsuleId).setValue(capsuleValues, (error, ref) -> {
            if (error != null) {
                throw new RuntimeException("Failed to create capsule: " + error.getMessage());
            }
        });
        return capsule;
    }

    public Memory createMemory(String capsuleId, CreateMemoryRequest request) {
        System.out.println("Creating memory for capsule: " + capsuleId);

        CompletableFuture<Memory> resultFuture = new CompletableFuture<>();

        try {
            capsuleReference.child(capsuleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Map<String, Object> capsuleMap = (Map<String, Object>) dataSnapshot.getValue();
                        if (capsuleMap == null) {
                            resultFuture.completeExceptionally(new ResourceNotFoundException("Capsule not found"));
                            return;
                        }

                        boolean isSealed = (boolean) capsuleMap.get("sealed");
                        if (isSealed) {
                            resultFuture.completeExceptionally(new IllegalStateException("Cannot add memories to sealed capsule"));
                            return;
                        }

                        // 새 메모리 생성
                        String memoryId = UUID.randomUUID().toString();
                        Memory memory = new Memory();
                        memory.setId(memoryId);
                        memory.setType(request.getType());
                        memory.setContent(request.getContent());
                        memory.setTimeCapsuleId(capsuleId);
                        memory.setCreatedAt(LocalDateTime.now());

                        Map<String, Object> memoryValues = new HashMap<>();
                        memoryValues.put("id", memory.getId());
                        memoryValues.put("type", memory.getType().toString());
                        memoryValues.put("content", memory.getContent());
                        memoryValues.put("timeCapsuleId", memory.getTimeCapsuleId());
                        memoryValues.put("createdAt", memory.getCreatedAt());

                        memoryReference.child(memoryId).setValue(memoryValues, (error, ref) -> {
                            if (error != null) {
                                System.err.println("Error creating memory: " + error.getMessage());
                                resultFuture.completeExceptionally(error.toException());
                            } else {
                                System.out.println("Memory created successfully with ID: " + memoryId);
                                resultFuture.complete(memory);
                            }
                        });

                    } catch (Exception e) {
                        resultFuture.completeExceptionally(e);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Database error: " + databaseError.getMessage());
                    resultFuture.completeExceptionally(databaseError.toException());
                }
            });

            return resultFuture.get(10, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("Failed to create memory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create memory: " + e.getMessage());
        }
    }

    public TimeCapsule sealCapsule(String capsuleId, LocalDateTime openDate) {
        System.out.println("Sealing capsule: " + capsuleId + " with open date: " + openDate);
        CompletableFuture<TimeCapsule> resultFuture = new CompletableFuture<>();

        try {
            capsuleReference.child(capsuleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Map<String, Object> capsuleMap = (Map<String, Object>) dataSnapshot.getValue();
                        if (capsuleMap == null) {
                            resultFuture.completeExceptionally(new ResourceNotFoundException("Capsule not found"));
                            return;
                        }

                        boolean isSealed = (boolean) capsuleMap.getOrDefault("sealed", false);
                        if (isSealed) {
                            resultFuture.completeExceptionally(new IllegalStateException("Capsule is already sealed"));
                            return;
                        }

                        // 업데이트할 값 설정
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("sealed", true);
                        updates.put("openDate", openDate.toString());

                        capsuleReference.child(capsuleId).updateChildren(updates, (error, ref) -> {
                            if (error != null) {
                                System.err.println("Error sealing capsule: " + error.getMessage());
                                resultFuture.completeExceptionally(error.toException());
                            } else {
                                // 캡슐 객체 생성 및 반환
                                TimeCapsule capsule = new TimeCapsule();
                                capsule.setId(capsuleId);
                                capsule.setName((String) capsuleMap.get("name"));
                                capsule.setCreator((String) capsuleMap.get("creator"));
                                capsule.setCreatedAtString((String) capsuleMap.get("createdAt"));
                                capsule.setSealed(true);
                                capsule.setOpenDateString(openDate.toString());

                                System.out.println("Successfully sealed capsule: " + capsuleId);
                                resultFuture.complete(capsule);
                            }
                        });

                    } catch (Exception e) {
                        System.err.println("Error processing capsule: " + e.getMessage());
                        resultFuture.completeExceptionally(e);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Database error: " + databaseError.getMessage());
                    resultFuture.completeExceptionally(databaseError.toException());
                }
            });

            return resultFuture.get(10, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("Failed to seal capsule: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to seal capsule: " + e.getMessage());
        }
    }

    public boolean isOpenable(String capsuleId) {
        System.out.println("Checking if capsule is openable: " + capsuleId);
        CompletableFuture<Boolean> resultFuture = new CompletableFuture<>();

        try {
            capsuleReference.child(capsuleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Map<String, Object> capsuleMap = (Map<String, Object>) dataSnapshot.getValue();
                        if (capsuleMap == null) {
                            resultFuture.completeExceptionally(new ResourceNotFoundException("Capsule not found"));
                            return;
                        }

                        boolean isSealed = (boolean) capsuleMap.getOrDefault("sealed", false);
                        String openDateStr = (String) capsuleMap.get("openDate");

                        if (!isSealed || openDateStr == null) {
                            resultFuture.complete(false);
                            return;
                        }

                        LocalDateTime openDate = LocalDateTime.parse(openDateStr);
                        boolean isOpenable = LocalDateTime.now().isAfter(openDate);

                        System.out.println("Capsule openable status: " + isOpenable);
                        resultFuture.complete(isOpenable);

                    } catch (Exception e) {
                        System.err.println("Error checking openable status: " + e.getMessage());
                        resultFuture.completeExceptionally(e);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Database error: " + databaseError.getMessage());
                    resultFuture.completeExceptionally(databaseError.toException());
                }
            });

            return resultFuture.get(10, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("Failed to check openable status: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to check openable status: " + e.getMessage());
        }
    }

    public List<Memory> getAllMemories(String capsuleId) {
        System.out.println("Getting all memories for capsule: " + capsuleId);
        CompletableFuture<List<Memory>> resultFuture = new CompletableFuture<>();

        try {
            memoryReference.orderByChild("timeCapsuleId").equalTo(capsuleId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                List<Memory> memories = new ArrayList<>();
                                for (DataSnapshot memorySnapshot : snapshot.getChildren()) {
                                    Map<String, Object> memoryMap = (Map<String, Object>) memorySnapshot.getValue();
                                    if (memoryMap != null) {
                                        Memory memory = new Memory();
                                        memory.setId((String) memoryMap.get("id"));
                                        memory.setType(MemoryType.valueOf((String) memoryMap.get("type")));
                                        memory.setContent((String) memoryMap.get("content"));
                                        memory.setTimeCapsuleId((String) memoryMap.get("timeCapsuleId"));
                                        memory.setCreatedAtString((String) memoryMap.get("createdAt"));
                                        memories.add(memory);
                                    }
                                }
                                System.out.println("Found " + memories.size() + " memories");
                                resultFuture.complete(memories);
                            } catch (Exception e) {
                                System.err.println("Error processing memories: " + e.getMessage());
                                resultFuture.completeExceptionally(e);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.err.println("Database error: " + databaseError.getMessage());
                            resultFuture.completeExceptionally(databaseError.toException());
                        }
                    });

            return resultFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Failed to get memories: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get memories: " + e.getMessage());
        }
    }

    public CompletableFuture<Memory> getMemory(String capsuleId, String memoryId) {
        CompletableFuture<Memory> future = new CompletableFuture<>();

        memoryReference.child(memoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Memory memory = dataSnapshot.getValue(Memory.class);
                if (memory == null || !memory.getTimeCapsuleId().equals(capsuleId)) {
                    future.completeExceptionally(new ResourceNotFoundException("Memory not found"));
                } else {
                    future.complete(memory);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("Database error: " + databaseError.getMessage()));
            }
        });

        return future;
    }

    public TimeCapsule getCapsule(String capsuleId) {
        CompletableFuture<TimeCapsule> future = new CompletableFuture<>();

        capsuleReference.child(capsuleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                TimeCapsule capsule = snapshot.getValue(TimeCapsule.class);
                future.complete(capsule);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        TimeCapsule capsule = future.join();
        if (capsule == null) {
            throw new ResourceNotFoundException("Capsule not found");
        }
        return capsule;
    }

    // 근처 캡슐 찾기
    public List<NearbyCapsuleResponse> findNearbyCapsules(double lat, double lng, double radiusInMeters) {
        System.out.println("Finding capsules near: " + lat + ", " + lng);
        CompletableFuture<List<NearbyCapsuleResponse>> resultFuture = new CompletableFuture<>();

        try {
            markerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        List<NearbyCapsuleResponse> nearbyCapsules = new ArrayList<>();

                        for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> markerMap = (Map<String, Object>) markerSnapshot.getValue();
                            if (markerMap != null) {
                                double markerLat = ((Number) markerMap.get("latitude")).doubleValue();
                                double markerLng = ((Number) markerMap.get("longitude")).doubleValue();

                                double distance = calculateDistance(lat, lng, markerLat, markerLng);

                                if (distance <= radiusInMeters) {
                                    String capsuleId = (String) markerMap.get("capsuleId");
                                    nearbyCapsules.add(NearbyCapsuleResponse.builder()
                                            .capsuleId(capsuleId)
                                            .latitude(markerLat)
                                            .longitude(markerLng)
                                            .distance(distance)
                                            .build());
                                }
                            }
                        }

                        resultFuture.complete(nearbyCapsules);
                    } catch (Exception e) {
                        resultFuture.completeExceptionally(e);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    resultFuture.completeExceptionally(databaseError.toException());
                }
            });

            return resultFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find nearby capsules: " + e.getMessage());
        }
    }

    // AR 마커 저장
    public ARMarker saveARMarker(String capsuleId, ARMarkerRequest request) {
        System.out.println("Saving AR marker for capsule: " + capsuleId);
        CompletableFuture<ARMarker> resultFuture = new CompletableFuture<>();

        try {
            String markerId = UUID.randomUUID().toString();
            ARMarker marker = new ARMarker();
            marker.setId(markerId);
            marker.setCapsuleId(capsuleId);
            marker.setLatitude(request.getLatitude());
            marker.setLongitude(request.getLongitude());
            marker.setMarkerData(request.getMarkerData());
            marker.setCreatedAt(LocalDateTime.now());

            Map<String, Object> markerValues = new HashMap<>();
            markerValues.put("id", marker.getId());
            markerValues.put("capsuleId", marker.getCapsuleId());
            markerValues.put("latitude", marker.getLatitude());
            markerValues.put("longitude", marker.getLongitude());
            markerValues.put("markerData", marker.getMarkerData());
            markerValues.put("createdAt", marker.getCreatedAt());

            markerReference.child(markerId).setValue(markerValues, (error, ref) -> {
                if (error != null) {
                    resultFuture.completeExceptionally(error.toException());
                } else {
                    resultFuture.complete(marker);
                }
            });

            return resultFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save AR marker: " + e.getMessage());
        }
    }

    // AR 마커 조회
    public ARMarker getARMarker(String capsuleId) {
        System.out.println("Getting AR marker for capsule: " + capsuleId);
        CompletableFuture<ARMarker> resultFuture = new CompletableFuture<>();

        try {
            markerReference.orderByChild("capsuleId").equalTo(capsuleId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                resultFuture.completeExceptionally(
                                        new ResourceNotFoundException("AR marker not found"));
                                return;
                            }

                            DataSnapshot markerSnapshot = dataSnapshot.getChildren().iterator().next();
                            Map<String, Object> markerMap =
                                    (Map<String, Object>) markerSnapshot.getValue();

                            ARMarker marker = new ARMarker();
                            marker.setId((String) markerMap.get("id"));
                            marker.setCapsuleId((String) markerMap.get("capsuleId"));
                            marker.setLatitude(((Number) markerMap.get("latitude")).doubleValue());
                            marker.setLongitude(((Number) markerMap.get("longitude")).doubleValue());
                            marker.setMarkerData((String) markerMap.get("markerData"));
                            marker.setCreatedAtString((String) markerMap.get("createdAt"));

                            resultFuture.complete(marker);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            resultFuture.completeExceptionally(databaseError.toException());
                        }
                    });

            return resultFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get AR marker: " + e.getMessage());
        }
    }

    // 거리 계산 헬퍼 메소드 (Haversine formula)
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371000; // 지구 반경 (미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
