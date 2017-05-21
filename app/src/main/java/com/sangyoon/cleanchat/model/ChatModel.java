package com.sangyoon.cleanchat.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatModel {
    private DatabaseReference ref;
    private List<Chat> chats = new ArrayList<>();

    private OnDataChangedListener onDataChangedListener;
    private OnCompleteListener onCompleteListener;

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.onDataChangedListener = listener;
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
    }

    public ChatModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.ref = database.getReference();
        this.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> newChats = new ArrayList<Chat>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot e : children) {
                    Chat chat = e.getValue(Chat.class);
                    newChats.add(chat);
                }

                chats = newChats;
                if (onDataChangedListener != null) {
                    onDataChangedListener.onDataChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

    public void sendMessage(String message) {
        DatabaseReference childRef = ref.push();
        childRef.setValue(Chat.newChat(message));
    }

    public void sendMessageWithImage(final String message, InputStream inputStream) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://ourguide-app.appspot.com").child("images");
        UploadTask uploadTask = storageReference.child(generateTempFilename()).putStream(inputStream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DatabaseReference databaseReference = ref.push();
                databaseReference.setValue(Chat.newChatWithImage(message, taskSnapshot.getDownloadUrl().toString()));

                if (onCompleteListener != null) {
                    onCompleteListener.onSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onCompleteListener != null) {
                    onCompleteListener.onFailure();
                }
            }
        });
    }

    private String generateTempFilename() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public String getMessage(int position) {
        return chats.get(position).message;
    }

    public String getImageURL(int position) {
        return chats.get(position).imageURL;
    }

    public int getMessageCount() {
        return chats.size();
    }
}











