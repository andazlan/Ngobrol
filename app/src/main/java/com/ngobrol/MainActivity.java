package com.ngobrol;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ngobrol.Member.LoginActivity;
import com.ngobrol.Message.Chat;
import com.ngobrol.Message.ChatAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.list_message)RecyclerView listMessage;
    @Bind(R.id.edt_message) TextInputEditText message;
    @Bind(R.id.btn_send) Button send;

    public static String TAG = "FirebaseTesting";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(Chat.class.getSimpleName().toLowerCase());
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mAuth.getCurrentUser() == null){
            signIn();
            setTitle(getString(R.string.app_name));
            return;
        }
        else{
            setTitle("Hi, " + mAuth.getCurrentUser().getDisplayName());
        }

        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        listMessage.setLayoutManager(linearManager);
        adapter = new ChatAdapter(new ArrayList<Chat>(), mAuth.getCurrentUser());
        listMessage.setAdapter(adapter);
        //listMessage.setre

        send.setOnClickListener(sendClick);

        showChats();
    }

    private void showChats() {
        Query chatQuery = myRef.orderByChild("timestamp");
        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                adapter.getChats().add(chat);
                adapter.notifyDataSetChanged();
                listMessage.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /*
                Toast.makeText(MainActivity.this, "onChildChanged ", Toast.LENGTH_SHORT).show();
                */
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*
                Toast.makeText(MainActivity.this, "onChildRemoved" + dataSnapshot.getValue(Chat.class).getName(), Toast.LENGTH_SHORT).show();
                adapter.getChats().remove(dataSnapshot.getValue(Chat.class));
                adapter.notifyDataSetChanged();
                */
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private View.OnClickListener sendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!message.getText().toString().trim().isEmpty()){
                writeNewMessage();
                listMessage.smoothScrollToPosition(adapter.getItemCount());
            }
        }
    };

    private void writeNewMessage() {
        Chat chat = new Chat(
                mAuth.getCurrentUser().getUid(),
                mAuth.getCurrentUser().getDisplayName(),
                message.getText().toString().trim(),
                System.currentTimeMillis());
        String key = myRef.push().getKey();
        Map<String, Object> child = new HashMap<>();
        child.put(key, chat.toMap());
        myRef.updateChildren(child);
        message.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        if (id == R.id.action_logout){
            mAuth.signOut();
            signIn();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser() == null){
            //Toast.makeText(MainActivity.this, "selesai", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void signIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
