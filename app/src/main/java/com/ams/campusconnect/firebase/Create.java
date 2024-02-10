/*
In this example, the Create class has a constructor that initializes a DatabaseReference object using the
FirebaseDatabase instance. The class also includes a createRecord() method that takes in a node parameter,
which is the name of the node where the new data will be inserted, and a data parameter, which is the actual
data to be inserted.

The createRecord() method then uses the push() method to generate a unique key for the new data and the
setValue() method to insert the data at that key in the specified node of the database.
 */
package com.ams.campusconnect.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create {
    private DatabaseReference mDatabase;

    public Create() {

    }

    public void createRecord(String node, Object data) {
        mDatabase = FirebaseDatabase.getInstance().getReference(node);
        mDatabase.setValue(data);
    }
}

/*
// To call in main
Create create = new Create();
create.createRecord("users", userObject);
 */



