package com.kubra.mesajlasmauygulamasi;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private List<String> userList;
    private String username;
    private RecyclerView userRV;
    private UserAdapter userAdapter;

    private EditText editText;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tanimla();
        listele();
    }

    public void tanimla() {
        username = getIntent().getExtras().getString("user");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userList = new ArrayList<>();


        userRV = findViewById(R.id.userRV);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        userRV.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(MainActivity.this, userList,MainActivity.this,username );
        userRV.setAdapter(userAdapter);



    }

    public void listele() {
        reference.child("Kullanıcılar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(!dataSnapshot.getKey().equals(username)){
                    userList.add(dataSnapshot.getKey());
                    Log.i("user: ",dataSnapshot.getKey());
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
