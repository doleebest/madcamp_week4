package madcamp4.Our_Beloved_KAIST.Service;

import com.google.firebase.database.*;
import madcamp4.Our_Beloved_KAIST.Domain.Memory;
import madcamp4.Our_Beloved_KAIST.Domain.MemoryType;
import madcamp4.Our_Beloved_KAIST.Domain.TimeCapsule;
import madcamp4.Our_Beloved_KAIST.Exception.ResourceNotFoundException;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateCapsuleRequest;
import madcamp4.Our_Beloved_KAIST.dto.request.CreateMemoryRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

@Service
public class TimeCapsuleService {
    private final DatabaseReference capsuleReference;
    private final DatabaseReference memoryReference;

    public TimeCapsuleService() {
        this.capsuleReference = FirebaseDatabase.getInstance().getReference("time_capsules");
        this.memoryReference = FirebaseDatabase.getInstance().getReference("memories");
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
        TimeCapsule capsule = getCapsule(capsuleId);
        if (capsule.isSealed()) {
            throw new IllegalStateException("Cannot add memories to sealed capsule");
        }

        String memoryId = UUID.randomUUID().toString();
        Memory memory = new Memory();
        memory.setId(memoryId);
        memory.setType(MemoryType.valueOf(request.getType().name())); // MemoryType 호환 수정
        memory.setContent(request.getContent());
        memory.setTimeCapsuleId(capsuleId);
        memory.setCreatedAt(LocalDateTime.now());

        Map<String, Object> memoryValues = new HashMap<>();
        memoryValues.put("id", memory.getId());
        memoryValues.put("type", memory.getType().toString());
        memoryValues.put("content", memory.getContent());
        memoryValues.put("timeCapsuleId", memory.getTimeCapsuleId());
        memoryValues.put("createdAt", memory.getCreatedAt().toString());

        memoryReference.child(memoryId).setValue(memoryValues, (error, ref) -> {
            if (error != null) {
                throw new RuntimeException("Failed to create memory: " + error.getMessage());
            }
        });
        return memory;
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
        CompletableFuture<List<Memory>> future = new CompletableFuture<>();
        List<Memory> memories = new ArrayList<>();

        memoryReference.orderByChild("timeCapsuleId").equalTo(capsuleId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Memory memory = child.getValue(Memory.class);
                            if (memory != null) {
                                memories.add(memory);
                            }
                        }
                        future.complete(memories);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });

        return future.join();
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
