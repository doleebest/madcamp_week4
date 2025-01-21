package madcamp4.Our_Beloved_KAIST.Service;

import com.google.firebase.database.*;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.MemoryType;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Exception.ResourceNotFoundException;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateMemoryRequest;
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

    @Autowired
    public TimeCapsuleService(
            @Qualifier("timeCapsulesReference") DatabaseReference capsuleReference,
            @Qualifier("memoriesReference") DatabaseReference memoryReference) {
        this.capsuleReference = capsuleReference;
        this.memoryReference = memoryReference;
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
        TimeCapsule capsule = getCapsule(capsuleId);
        if (capsule.isSealed()) {
            throw new IllegalStateException("Capsule is already sealed");
        }

        capsule.setSealed(true);
        capsule.setOpenDate(openDate);

        Map<String, Object> updates = new HashMap<>();
        updates.put("sealed", true);
        updates.put("openDate", openDate.toString());

        capsuleReference.child(capsuleId).updateChildren(updates, (error, ref) -> {
            if (error != null) {
                throw new RuntimeException("Failed to seal capsule: " + error.getMessage());
            }
        });
        return capsule;
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

    public boolean isOpenable(String capsuleId) {
        TimeCapsule capsule = getCapsule(capsuleId);
        return capsule.isSealed() && LocalDateTime.now().isAfter(capsule.getOpenDate());
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
}
