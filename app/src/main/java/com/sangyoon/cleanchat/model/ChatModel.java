package com.sangyoon.cleanchat.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatModel {
    private DatabaseReference ref;
    private List<Chat> chats = new ArrayList<>(); // DIP ( 의존성 역전 법칙 )

    private OnDataChangedListener onDataChangedListener;

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.onDataChangedListener = listener;
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











