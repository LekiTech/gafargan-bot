package core.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import core.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static core.config.Env.isRunningFromJar;

public class DataStorage {

    private static DataStorage instance;
    private Firestore db;

    private DataStorage() {
        try {
            this.db = initFirestore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataStorage instance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void createUser(Long chatId) {
        if (getUserData(chatId) != null) {
            return;
        }
        DocumentReference docRef = db.collection("users").document(chatId.toString());
        // Add document data
        Map<String, Object> data = new HashMap<>();
        data.put("chatId", chatId);
        data.put("createdAt", new Timestamp(System.currentTimeMillis()));
        //asynchronously write data
        docRef.set(data);
    }

    public void saveSearch(Long chatId, String userMessage) {
        UUID doc = UUID.randomUUID();
        DocumentReference docRef = db.collection("searches").document(doc.toString());
        // Add document data
        Map<String, Object> data = new HashMap<>();
        data.put("chatId", chatId);
        data.put("createdAt", new Timestamp(System.currentTimeMillis()));
        data.put("value", userMessage);
        //asynchronously write data
        docRef.set(data);
    }

    public void saveSelectedDictionary(Long chatId, String selectedDictionary) {
        DocumentReference docRef = db.collection("lastSelectedDictionary").document(chatId.toString());
        // Add document data
        Map<String, Object> data = new HashMap<>();
        data.put("dictionary", selectedDictionary);
        data.put("createdAt", new Timestamp(System.currentTimeMillis()));
        //asynchronously write data
        docRef.set(data);
    }

    public String getLastSelectedDictionary(Long chatId) {
        DocumentReference docRef = db.collection("lastSelectedDictionary").document(chatId.toString());
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                // System.out.println("Document data: " + document.getData());
                return (String) document.getData().get("dictionary");
                //.get("language", String.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> getUserData(Long chatId) {
        DocumentReference docRef = db.collection("users").document(chatId.toString());
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // future.get() blocks on response
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                System.out.println("Document data: " + document.getData());
                return document.getData();
                //.get("language", String.class);
            } else {
                System.out.println("No such document!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Firestore initFirestore() {
        try {
            if (isRunningFromJar()) {
                String encodedCredentials = System.getenv("GOOGLE_CREDENTIALS_BASE64");
                if (encodedCredentials == null || encodedCredentials.isEmpty()) {
                    throw new IllegalStateException("Google credentials environment variable is not set or is empty");
                }
                byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
                try (InputStream serviceAccount = new ByteArrayInputStream(decodedCredentials)) {
                    GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                    FirebaseOptions options = new FirebaseOptions.Builder()
                            .setCredentials(credentials)
                            .build();
                    FirebaseApp.initializeApp(options);
                    return FirestoreClient.getFirestore();
                }
            } else {
                InputStream serviceAccount = Main.class.getClassLoader().getResourceAsStream("gafargan-bot-firebase-adminsdk-3uwov-e042705c03.json");
                assert serviceAccount != null;
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);
                return FirestoreClient.getFirestore();
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize Firestore: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Set<Long> getAllChatId() throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
        return query.get().getDocuments().stream().map(document -> (Long) document.getData().get("chatId")).collect(Collectors.toSet());
    }
}