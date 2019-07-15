package com.kubra.mesajlasmauygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GirisActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(final String userId, String registrationId) {
                System.out.println("UserID:"+userId);
                UUID uuıd=UUID.randomUUID();
                final String uuidString=uuıd.toString();

                DatabaseReference newReference= firebaseDatabase.getReference("PlayersID");
                newReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> playerIDsFromServer=new ArrayList<>();

                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            HashMap<String,String> hashMap=(HashMap<String, String>)ds.getValue();
                            String currentPlayerID=hashMap.get("playerID");

                            playerIDsFromServer.add(currentPlayerID);
                        }

                        if(!playerIDsFromServer.contains(userId)){
                            reference.child("PlayersID").child(uuidString).child("playerID").setValue(userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }


        });
        tanimla();
    }

    public void tanimla(){
        editText=findViewById(R.id.editText1);
        button=findViewById(R.id.button1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=editText.getText().toString();
                ekle(username);
            }
        });
    }

    public void ekle(final String user){
        reference.child("Kullanıcılar").child(user).child("username").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "kayıt eklendi", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GirisActivity.this,MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                }
            }
        });
    }
}
