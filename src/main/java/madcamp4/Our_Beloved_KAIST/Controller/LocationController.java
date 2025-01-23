package madcamp4.Our_Beloved_KAIST.Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import madcamp4.Our_Beloved_KAIST.Domain.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final Firestore firestore;

    public LocationController() {
        this.firestore = FirestoreClient.getFirestore();
    }

    @PostMapping
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {
        DocumentReference docRef = firestore.collection("locations").document();
        docRef.set(location);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() throws ExecutionException, InterruptedException {
        List<Location> locations = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection("locations").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            locations.add(document.toObject(Location.class));
        }
        return ResponseEntity.ok(locations);
    }
}