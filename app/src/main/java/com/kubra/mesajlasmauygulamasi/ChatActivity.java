package com.kubra.mesajlasmauygulamasi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String othername,username;
    TextView textView;
    ImageView ımageViewBack,ımageViewSend;
    //EditText editText;
    EditText chatTextGonder;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView recyclerView;
    MesajAdapter mesajAdapter;
    List<MesajModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        tanimla();
        mesajAl();

    }

    public void tanimla(){
        list=new ArrayList<>();
        username = getIntent().getExtras().getString("username");
        othername = getIntent().getExtras().getString("othername");



        Log.i("değer: ",username+othername);
        textView = findViewById(R.id.chatText);
        ımageViewBack = findViewById(R.id.backImage);
        ımageViewSend =findViewById(R.id.sendImage);
        //editText = findViewById(R.id.chatTextGonder);
        chatTextGonder=findViewById(R.id.chatTextGonder);


        textView.setText(othername);
        ımageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                intent.putExtra("user",username);
                startActivity(intent);
            }
        });
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();

        ımageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = chatTextGonder.getText().toString();
                chatTextGonder.setText("");
                mesajGonder(mesaj);
            }
        });
        recyclerView = findViewById(R.id.RecyView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        mesajAdapter = new MesajAdapter(ChatActivity.this,list,ChatActivity.this,username);
        recyclerView.setAdapter(mesajAdapter);
    }
    public void mesajGonder(String text){
        final String key=reference.child("Mesajlar").child(username).child(othername).push().getKey();
        final Map map=new HashMap();
        map.put("text",text);
        map.put("from",username);


        reference.child("Mesajlar").child(username).child(othername).child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("Mesajlar").child(othername).child(username).child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
    }

    public void mesajAl(){
        reference.child("Mesajlar").child(username).child(othername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    MesajModel mesajModel = dataSnapshot.getValue(MesajModel.class);
                    //Log.i("mesaj",mesajModel.toString());
                    list.add(mesajModel);
                    mesajAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(list.size()-1);
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

        DatabaseReference newReference= firebaseDatabase.getReference("PlayersID");
        newReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap=(HashMap<String, String>)ds.getValue();

                    String playerID=hashMap.get("playerID");
                    System.out.println("playerID server :"+playerID);


                    try {
                        OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+chatTextGonder+"'}, 'include_player_ids': ['" + playerID + "']}"), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
