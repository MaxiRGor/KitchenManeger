package com.distinct.kitchenmanager.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.distinct.kitchenmanager.MainActivity;
import com.distinct.kitchenmanager.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAuthActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REGISTER = 1505;
    private boolean isRegistrationStartedAndNotFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        String fridgeId = getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.id_fridge_preferences_field), "");
        checkUserAuth(fridgeId);
    }

    private void checkUserAuth(String fridgeId) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null || fridgeId.equals("")) {
            isRegistrationStartedAndNotFinished = true;
            createSignInIntent();
        } else {
            startMainActivity(fridgeId);
        }
    }

    private void startMainActivity(String fridgeId) {
        isRegistrationStartedAndNotFinished = false;
        Intent intent = new Intent(FirebaseAuthActivity.this, MainActivity.class);
        intent.putExtra(getString(R.string.id_fridge_preferences_field), fridgeId);
        intent.putExtra(getString(R.string.user_name_preferences_field), getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.user_name_preferences_field), ""));
        startActivity(intent);
        finish();
    }

    public void createSignInIntent() {
        //code below belongs to firebase-ui-auth
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(),
                REQUEST_CODE_REGISTER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REGISTER) {
            IdpResponse.fromResultIntent(data);                                                     //add a new user to authenticated users
            if (resultCode == RESULT_OK) {
                continueRegistration();
            } else {
                Toast.makeText(getApplicationContext(), R.string.oops_try_again, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void continueRegistration() {
        findViewById(R.id.registration_layout).setVisibility(View.VISIBLE);

        EditText userNameEditText = findViewById(R.id.registration_user_name_edit_text);
        Switch inUseSwitch = findViewById(R.id.registration_is_app_in_use_switch);
        Button authorizeButton = findViewById(R.id.registration_authorize_button);
        EditText fridgeIdEditText = findViewById(R.id.registration_fridge_id_edit_text);

        inUseSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                fridgeIdEditText.setVisibility(View.VISIBLE);
            } else fridgeIdEditText.setVisibility(View.GONE);
        });

        authorizeButton.setOnClickListener(view -> {
            if (inUseSwitch.isChecked()) {
                if (validateForm(userNameEditText)) return;
                if (validateForm(fridgeIdEditText)) return;
                checkIfFridgeExists(fridgeIdEditText, userNameEditText.getText().toString());
            } else {
                if (validateForm(userNameEditText)) return;
                createNewFridge(userNameEditText.getText().toString());
            }
        });
    }

    private boolean validateForm(EditText editText) {
        String text = editText.getText().toString();

        boolean valid = true;
        if (TextUtils.isEmpty(text)) {
            editText.setError(getResources().getString(R.string.is_empty));
            valid = false;
        } else {
            editText.setError(null);
        }
        return !valid;
    }

    private void checkIfFridgeExists(EditText fridgeIdEditText, final String userName) {
        String fridgeId = fridgeIdEditText.getText().toString();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();                                  //below: search document with documentId() == fridgeId in collection "fridges"
        firestore.collection(getString(R.string.firestore_collection_fridges)).whereEqualTo(FieldPath.documentId(), fridgeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {                                                                  //if phone knocked to database and found a collection "fridges"
                if (task.getResult() != null && task.getResult().size() != 0) {                         //if search brought not null result
                    addUserToFridge(fridgeId, userName);
                } else
                    fridgeIdEditText.setError(getResources().getString(R.string.incorrect_fridge_id));
            }
        });
    }

    private void createNewFridge(String userName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> fridgeInitData = new HashMap<>();
        fridgeInitData.put(getString(R.string.firestore_array_field_users), Collections.singletonList(userName));                           //add a new one-component string list to a map
        DocumentReference fridge = firestore.collection(getString(R.string.firestore_collection_fridges)).document();                       //because arguments in document is absent (id==null), it creates a new document with random id
        fridge.set(fridgeInitData);                                                                                                         //and add to this doc info from HashMap
        writeSharedPreference(userName, R.string.user_name_preferences_field);
        writeSharedPreference(fridge.getId(), R.string.id_fridge_preferences_field);                                                                                              //save an id of this new created document to user device
        startMainActivity(fridge.getId());
    }


    private void addUserToFridge(String fridgeId, String userName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(getString(R.string.firestore_collection_fridges)).document(fridgeId).update(getString(R.string.firestore_array_field_users), FieldValue.arrayUnion(userName));
        writeSharedPreference(userName, R.string.user_name_preferences_field);
        writeSharedPreference(fridgeId, R.string.id_fridge_preferences_field);
        startMainActivity(fridgeId);
    }

    private void writeSharedPreference(String value, int keyResourceId) {
        SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(keyResourceId), value);
        editor.apply();
    }

    public void delete() {
        AuthUI.getInstance().delete(this);
    }

    @Override
    protected void onStop() {
        if (isRegistrationStartedAndNotFinished) {
            delete();
        }
        super.onStop();
    }


}