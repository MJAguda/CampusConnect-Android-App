package com.example.campusconnect;

// Import the required Firebase database package
import com.google.firebase.database.*;

public class Transfer {

    private String source;
    private String destination;

    // Create an instance of the Firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public Transfer(DatabaseReference source, DatabaseReference destination){
        this.source = String.valueOf(source);
        this.source = String.valueOf(destination);
    }

    // Method to copy the data from 'fromPath' to 'toPath'
    public void copyRecord(DatabaseReference source, DatabaseReference destination){
        // Attach a listener to the 'fromPath' node to read the data
        source.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Write the read data to the 'toPath' node
                destination.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        // Check for any error while copying the data
                        if (databaseError != null) {
                            System.out.println("Data could not be copied: " + databaseError.getMessage());
                        } else {
                            Delete delete = new Delete();
                            delete.deleteRecord("305113/employee", "123456");
                            System.out.println("Data copied successfully.");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Data copying was cancelled.");
            }
        });
    }
}