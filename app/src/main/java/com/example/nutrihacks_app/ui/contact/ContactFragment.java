package com.example.nutrihacks_app.ui.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.nutrihacks_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ContactFragment extends Fragment {
    // Firebase Database Connection Instance
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    // Save Button
    private Button btnSave;
    // User Input
    private EditText firstName, lastName, email, age;
    private long uid = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the View Layout
        View root = inflater.inflate(R.layout.fragment_contact, container, false);

        // Get Button View
        btnSave = root.findViewById(R.id.btnSave);
        // Get User Input Views
        firstName = (EditText) root.findViewById(R.id.firstName);
        lastName = (EditText) root.findViewById(R.id.lastName);
        email = (EditText) root.findViewById(R.id.email);
        age = (EditText) root.findViewById(R.id.age);

        // When user submits the form
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Validate user input
                if (!validateInput(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), age.getText().toString())) {
                    Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                    return;
                }

                // 2. Make user object
                User user = new User(firstName.getText().toString().trim(),
                        lastName.getText().toString().trim(),
                        email.getText().toString().trim(),
                        age.getText().toString().trim());


                // 3. Try to store user in Firebase
                writeNewUser(String.valueOf(uid), user);
            }
        });

        return root;
    }

    // Tries to store user object to Firebase
    private void writeNewUser(String userId, User user) {
        // Store user in Firebase DB inside 'users' collection
        database.child("users").child(userId).setValue(user)
                // On success display success toast
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "User inserted successfully", Toast.LENGTH_LONG).show();
                    }
                })
                // On failure display failure toast
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed, Something went wrong :(", Toast.LENGTH_LONG).show();
                    }
                });
        uid++;
    }

    // Checks if user has filled every form field
    private Boolean validateInput(String firsName, String lastName, String email, String age) {
        if (firsName.equals("") || lastName.equals("") || email.equals("") || age.equals("")) {
            return false;
        }
        return true;
    }

}