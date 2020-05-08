package com.distinct.kitchenmanager.ui.account;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.distinct.kitchenmanager.MainActivity;
import com.distinct.kitchenmanager.R;
import com.distinct.kitchenmanager.ui.FirebaseAuthActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.Callable;

public class AccountFragment extends Fragment {

    private TextView userNameTextView;
    private Button copyFridgeIdButton;
    private Button signOutButton;
    private Button deleteAccountButton;
    private TextView fridgeUsersTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        findViews(root);
        setUserNameText();
        setCopyFridgeIdButton();
        setFridgeUsersText();
        setOnClickListeners();

        return root;
    }

    private void findViews(View root) {
        userNameTextView = root.findViewById(R.id.user_name_text_view);
        copyFridgeIdButton = root.findViewById(R.id.copy_fridge_id_button);
        fridgeUsersTextView = root.findViewById(R.id.fridge_users_text_view);
        signOutButton = root.findViewById(R.id.sign_out_button);
        deleteAccountButton = root.findViewById(R.id.delete_account_button);
    }

    private void setUserNameText() {
        userNameTextView.setText(String.format(getString(R.string.hello_user), MainActivity.getUserName()));
    }

    private void setCopyFridgeIdButton() {
        copyFridgeIdButton.setText(MainActivity.getFridgeId());
        copyFridgeIdButton.setOnClickListener(view -> {
            if (getActivity() != null && getActivity().getSystemService(Context.CLIPBOARD_SERVICE) != null) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(String.format(getString(R.string.copied), MainActivity.getFridgeId()), MainActivity.getFridgeId());
                if (clipboard != null)
                    clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), clip.getDescription().getLabel(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setFridgeUsersText() {
        FirebaseFirestore.getInstance().collection(getString(R.string.firestore_collection_fridges)).document(MainActivity.getFridgeId()).get().addOnSuccessListener(documentSnapshot -> {
            List<String> users;
            users = (List<String>) documentSnapshot.get(getString(R.string.firestore_array_field_users));
            if (users != null) {
                String usersInOneString = users.toString();
                usersInOneString = usersInOneString.substring(1, usersInOneString.length() - 1);   //to cut [ at the beginning and ] at the end
                fridgeUsersTextView.setText(String.format(getString(R.string.users_now), users.size(), usersInOneString));
            }
        });
    }


    private void setOnClickListeners() {
        signOutButton.setOnClickListener(view -> showConfirmDialog(() -> {
            signOut();
            return null;
        }));

        deleteAccountButton.setOnClickListener(view -> showConfirmDialog(() -> {
            deleteAccount();
            return null;
        }));
    }


    private void showConfirmDialog(Callable<Void> task) {
        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.confirm_question))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    try {
                        task.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .show();
    }


    private void startAuthActivity() {
        Intent intent = new Intent(getContext(), FirebaseAuthActivity.class);
        startActivity(intent);

        if (getActivity() != null)
            getActivity().finish();
    }


    private void signOut() {
        if (getContext() != null)
            AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(task -> startAuthActivity());
    }

    private void deleteAccount() {
        if (getContext() != null)
            AuthUI.getInstance().delete(getContext()).addOnCompleteListener(task -> startAuthActivity());
    }

}
