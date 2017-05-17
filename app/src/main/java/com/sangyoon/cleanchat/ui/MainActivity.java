package com.sangyoon.cleanchat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.sangyoon.cleanchat.R;
import com.sangyoon.cleanchat.model.ChatModel;
import com.sangyoon.cleanchat.model.OnDataChangedListener;

public class MainActivity extends AppCompatActivity {

    private ChatModel model = new ChatModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText chatEdit = (EditText) findViewById(R.id.chat_edit);

        Button chatButton = (Button) findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatEdit.getText().toString();
                if (message.length() > 0) {
                    model.sendMessage(message);
                }
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecyclerView.Adapter<ChatHolder>() {
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.recycler_item_chat, parent, false);
                return new ChatHolder(view);
            }

            @Override
            public void onBindViewHolder(ChatHolder holder, int position) {
                String message = model.getMessage(position);
                holder.setText(message);

                String imageUrl = model.getImageURL(position);
                holder.setImage(imageUrl);


            }

            @Override
            public int getItemCount() {
                return model.getMessageCount();
            }
        });


        model.setOnDataChangedListener(new OnDataChangedListener() {
            @Override
            public void onDataChanged() {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();


        dialog.show();
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;


        public ChatHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.chat_text_view);
            imageView = (ImageView) itemView.findViewById(R.id.chat_image_view);
        }

        public void setText(String text) {
            textView.setText(text);
        }

        public void setImage(String imageUrl) {
            Glide.with(MainActivity.this)
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}