package com.example.atamerica.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.ParentActivity;
import com.example.atamerica.R;
import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.databinding.FragmentLoginBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.AppUserModel;
import com.example.atamerica.ui.signup.SignUpFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import JavaLibrary.AESUtil;

public class LoginFragment extends Fragment {

    private AESUtil hashFunction;

    private EditText editTextTextEmailAddress, editTextTextPassword;
    private CheckBox checkboxRememberMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind binding
        com.example.atamerica.databinding.FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // request for current activity to allow internet connection
        // this must be in every activity/fragments
        ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Bind views
        TextView labelForgot = (TextView) rootView.findViewById(R.id.labelForgot);
        TextView labelSignUp = (TextView) rootView.findViewById(R.id.labelSignUp);
        editTextTextEmailAddress = (EditText) rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) rootView.findViewById(R.id.editTextTextPassword);
        checkboxRememberMe = (CheckBox) rootView.findViewById(R.id.checkboxRememberMe);
        Button buttonSignIn = (Button) rootView.findViewById(R.id.buttonSignIn);

        hashFunction = new AESUtil();

        labelSignUp.setOnClickListener(view -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.authenticateContainer, new SignUpFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        buttonSignIn.setOnClickListener(view -> SignIn());

        labelForgot.setOnClickListener(view -> CallSnackBar("SHI... TO BAD."));

        return rootView;
    }

    private void SignIn() {
        // Check required fields
        boolean isRequired = false;
        if (TextUtils.isEmpty(editTextTextEmailAddress.getText())) {
            editTextTextEmailAddress.setError("Email is required.");
            isRequired = true;
        }
        if (TextUtils.isEmpty(editTextTextPassword.getText())) {
            editTextTextPassword.setError("Password is required.");
            isRequired = true;
        }

        if (isRequired) return;

        String email = editTextTextEmailAddress.getText().toString();
        String password = editTextTextPassword.getText().toString();
        boolean isRemember = checkboxRememberMe.isChecked();

        AppUserModel model = DataHelper.Query.ReturnAsObject("SELECT * FROM AppUser WHERE Email = ?; ", AppUserModel.class, new Object[] { email });
        if (model != null) {
            // Password confirmation check
            String passwordSaltPepper = model.PasswordSalt + password + model.PasswordPepper;

            String encryptedPassword = hashFunction.encrypt(passwordSaltPepper);

            if (Objects.equals(encryptedPassword, model.PasswordHash)) {
                // Check if user checked remember me
                if (isRemember) {
                    // Store encryption
                    String encryptedEmail = hashFunction.encrypt(email);
                    String encryptedEncryptedPassword = hashFunction.encrypt(encryptedPassword);

                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("com.example.atamerica", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getResources().getString(R.string.user_email_hash), encryptedEmail);
                    editor.putString(getResources().getString(R.string.user_password_hash), encryptedEncryptedPassword);
                    editor.putBoolean(getResources().getString(R.string.user_remember_hash), true);
                    editor.apply();
                }

                AccountManager.User = model;

                Intent intent = new Intent(getActivity(), ParentActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
            else {
                editTextTextEmailAddress.setError("Email might be wrong.");
                editTextTextPassword.setError("Password might be wrong.");
                CallSnackBar("WARNING, Email or password wrong.");
            }
        }
        else {
            editTextTextEmailAddress.setError("Email might be wrong.");
            editTextTextPassword.setError("Password might be wrong.");
            CallSnackBar("WARNING, Email or password wrong.");
        }
    }

    private void CallSnackBar(String text) {
        Snackbar.make(requireActivity().findViewById(R.id.authenticateContainer), text, Snackbar.LENGTH_LONG).show();
    }
}