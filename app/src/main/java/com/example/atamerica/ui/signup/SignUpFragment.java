package com.example.atamerica.ui.signup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentSignUpBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.AppUserModel;
import com.example.atamerica.ui.login.LoginFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import JavaLibrary.AESUtil;

public class SignUpFragment extends Fragment {

    private EditText editTextTextFullName, editTextTextEmailAddress, editTextTextPhone, editTextTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind binding
        com.example.atamerica.databinding.FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Bind views
        TextView labelSignUp = (TextView) rootView.findViewById(R.id.labelSignUp);
        editTextTextFullName        = (EditText) rootView.findViewById(R.id.editTextTextFullName);
        editTextTextEmailAddress    = (EditText) rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone           = (EditText) rootView.findViewById(R.id.editTextTextPhone);
        editTextTextPassword        = (EditText) rootView.findViewById(R.id.editTextTextPassword);
        Button buttonSignUp = (Button) rootView.findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(view -> SignUp());

        labelSignUp.setOnClickListener(view -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.authenticateContainer, new LoginFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void SignUp() {
        // Check required fields
        boolean isRequired = false;
        if (TextUtils.isEmpty(editTextTextFullName.getText())) {
            editTextTextFullName.setError("Full name is required.");
            isRequired = true;
        }
        if (TextUtils.isEmpty(editTextTextEmailAddress.getText())) {
            editTextTextEmailAddress.setError("Email is required.");
            isRequired = true;
        }
        if (TextUtils.isEmpty(editTextTextPhone.getText())) {
            editTextTextPhone.setError("Phone number is required.");
            isRequired = true;
        }
        if (TextUtils.isEmpty(editTextTextPassword.getText())) {
            editTextTextPassword.setError("Password is required.");
            isRequired = true;
        }

        if (isRequired) return;

        String fullName = editTextTextFullName.getText().toString();
        String email = editTextTextEmailAddress.getText().toString();
        String phoneNumber = editTextTextPhone.getText().toString();
        String password = editTextTextPassword.getText().toString();

        AppUserModel model = DataHelper.Query.ReturnAsObject("SELECT * FROM AppUser WHERE Email = ?; ", AppUserModel.class, new Object[] { email });
        if (model == null) {
            // Generate password salt and pepper
            String passwordSalt = getAlphaNumericString();
            String passwordPepper = getAlphaNumericString();

            // Hash salt + password + pepper
            AESUtil hashFunction = new AESUtil();
            String passwordSaltPepper = passwordSalt + password + passwordPepper;
            String encryptedPassword = hashFunction.encrypt(passwordSaltPepper);

            boolean execute = DataHelper.Query.ExecuteNonQuery("INSERT INTO AppUser (Email, FullName, ContactNumber1, PasswordSalt, PasswordPepper, PasswordHash) VALUES (?, ?, ?, ?, ?, ?); ",
                    new Object[] { email, fullName, phoneNumber, passwordSalt, passwordPepper, encryptedPassword });

            // Transaction success
            if (execute) {
                // Create snack-bar
                Snackbar.make(requireActivity().findViewById(R.id.authenticateContainer), "SUCCESS, Please Login Again.", Snackbar.LENGTH_LONG).show();

                // Call login again
                FragmentManager manager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.authenticateContainer, new LoginFragment(), null);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            else {
                // Create snack-bar
                Snackbar.make(requireActivity().findViewById(R.id.authenticateContainer), "ERROR, Please Try Again Later.", Snackbar.LENGTH_LONG).show();
            }
        }
        else {
            editTextTextEmailAddress.setError("Email already exist.");

            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(requireActivity());
            dialog.setTitle("Warning");
            dialog.setMessage("Email already exist.");
            dialog.setNeutralButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
            dialog.show();
        }
    }

    private String getAlphaNumericString() {
        int n = 16;
        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, StandardCharsets.UTF_8);

        // Create a StringBuffer to store the result
        StringBuilder r = new StringBuilder();

        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < randomString.length(); k++) {
            char ch = randomString.charAt(k);

            if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {
                r.append(ch);
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
}