/*
In this example, the Read class has a constructor that initializes a DatabaseReference object using
the FirebaseDatabase instance. The class also includes a readRecord() method that takes in a node
parameter, which is the name of the node where the data will be retrieved, and a listener object,
which is an interface to handle the response of the data.

The readRecord() method uses the addListenerForSingleValueEvent() method to attach a ValueEventListener
to the specified node in the database. When data is retrieved, the onDataChange() method of the listener
is called and the data is passed as a DataSnapshot object to the onSuccess() method of the listener interface.
If there is an error, the onCancelled()S method of the listener is called and the error is passed as a
DatabaseError object to the onFailure() method of the listener interface.
 */
package com.ams.campusconnect.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Read {
    private final DatabaseReference mDatabase;

    public Read() {  mDatabase = FirebaseDatabase.getInstance().getReference(); }

    public void readRecord(String node, final OnGetDataListener listener) {
        mDatabase.child(node).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }
    public interface OnGetDataListener {
        void onSuccess(DataSnapshot dataSnapshot);
        void onFailure(DatabaseError databaseError);
    }
}

/*
// To call in main
Read read = new Read();
read.readRecord("users", new Read.OnGetDataListener() {
    @Override
    public void onSuccess(DataSnapshot dataSnapshot) {
        // handle data here
    }

    @Override
    public void onFailure(DatabaseError databaseError) {
        // handle error here
    }
});

 */