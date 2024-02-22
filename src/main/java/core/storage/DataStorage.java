package core.storage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import core.main.Main;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
//        UUID doc = UUID.randomUUID();
//        DocumentReference docRef = db.collection("searches").document(doc.toString());
//        // Add document data
//        Map<String, Object> data = new HashMap<>();
//        data.put("chatId", chatId);
//        data.put("createdAt", new Timestamp(System.currentTimeMillis()));
//        data.put("value", userMessage);
//        //asynchronously write data
//        docRef.set(data);
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

    private Firestore initFirestore() throws IOException {
        // InputStream serviceAccount = new FileInputStream("path/to/serviceAccount.json");
        InputStream serviceAccount = Main.class.getClassLoader().getResourceAsStream("gafargan-bot-facc575fa9c7.json");
        assert serviceAccount != null;
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();
    }

    public Set<Long> getAllChatId() throws Exception {
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
        return query.get().getDocuments().stream().map(document -> (Long) document.getData().get("chatId")).collect(Collectors.toSet());
    }
}