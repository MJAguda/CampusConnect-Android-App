/*
In this example, the Update class has a constructor that initializes a DatabaseReference object using
the FirebaseDatabase instance. The class also includes an updateRecord() method that takes in a node
parameter, which is the name of the node where the data will be updated, a key parameter, which is
the unique key of the record to be updated and a newData parameter, which is the new data that will
replace the existing data.

The updateRecord() method uses the child() method to navigate to the specified node and key of the
database, and then uses the setValue() method to update the data at that key in the specified node
of the database.
 */

package com.ams.campusconnect.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Update {
    private DatabaseReference mDatabase;

    public Update() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void updateRecord(String node, String key, Object newData) {
        mDatabase.child(node).child(key).setValue(newData);
    }
}

/*
// To call in main
Update update = new Update();
update.updateRecord("users", key, newUserObject);
 */