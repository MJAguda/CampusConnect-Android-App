/*
In this example, the Delete class has a constructor that initializes a DatabaseReference object using
the FirebaseDatabase instance. The class also includes a deleteRecord() method that takes in a node
parameter, which is the name of the node where the record is located, and a key parameter, which is
the unique key of the record to be deleted.

The deleteRecord() method uses the child() method to navigate to the specified node and key of the
database, and then uses the removeValue() method to delete the record at that key in the specified
node of the database.
 */
package com.ams.campusconnect.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Delete {
    private final DatabaseReference mDatabase;

    public Delete() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void deleteRecord(String node, String key) {
        mDatabase.child(node).child(key).removeValue();
    }
}

/*
// To call in main
Delete delete = new Delete();
delete.deleteRecord("users", key);
 */
