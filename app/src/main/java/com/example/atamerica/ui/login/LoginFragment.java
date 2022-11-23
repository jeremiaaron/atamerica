package com.example.atamerica.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.ParentActivity;
import com.example.atamerica.R;
import com.example.atamerica.cache.AccountManager;
import com.example.atamerica.databinding.FragmentLoginBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.AppUserModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.signup.SignUpFragment;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Objects;
import java.util.concurrent.Callable;

import JavaLibrary.AESUtil;

public class LoginFragment extends Fragment {

    private class LoginTask implements Callable<AppUserModel> {
        @Override
        public AppUserModel call() {
            try {
                AppUserModel user = DataHelper.Query.ReturnAsObject("SELECT * FROM AppUser WHERE Email = ?; ", AppUserModel.class, new Object[] { email });
                if (user != null) {
                    // Tests salt and pepper with given password
                    String passwordSaltPepper = user.PasswordSalt + password + user.PasswordPepper;
                    String encryptedPassword = hashFunction.encrypt(passwordSaltPepper);

                    if (Objects.equals(encryptedPassword, user.PasswordHash)) {
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

                        return user;
                    }
                    else {
                        return null;
                    }
                }

                return null;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private AESUtil     hashFunction;

    private EditText    editTextTextEmailAddress, editTextTextPassword;
    private CheckBox    checkboxRememberMe;

    private String      email, password;
    private boolean     isRemember;

    private CircularProgressIndicator progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind binding
        com.example.atamerica.databinding.FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Bind views
        TextView labelForgot        = (TextView) rootView.findViewById(R.id.labelForgot);
        TextView labelSignUp        = (TextView) rootView.findViewById(R.id.labelSignUp);
        editTextTextEmailAddress    = (EditText) rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword        = (EditText) rootView.findViewById(R.id.editTextTextPassword);
        checkboxRememberMe          = (CheckBox) rootView.findViewById(R.id.checkboxRememberMe);
        Button buttonSignIn         = (Button) rootView.findViewById(R.id.buttonSignIn);
        progressIndicator           = (CircularProgressIndicator) requireActivity().findViewById(R.id.progressIndicator);

        hashFunction = new AESUtil();

        labelSignUp.setOnClickListener(view -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenticateContainer, new SignUpFragment(), null);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        buttonSignIn.setOnClickListener(view -> {
            progressIndicator.setVisibility(View.VISIBLE);
            SignIn();
        });
        labelForgot.setOnClickListener(view -> Toast.makeText(getContext(), "SHI... TOO BAD", Toast.LENGTH_SHORT).show());

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

        if (isRequired)  {
            progressIndicator.setVisibility(View.GONE);
            return;
        }

        this.email          = editTextTextEmailAddress.getText().toString();
        this.password       = editTextTextPassword.getText().toString();
        this.isRemember     = checkboxRememberMe.isChecked();

        new TaskRunner().executeAsyncPool(new LoginTask(), (data) -> {

            if (data != null) {
                AccountManager.User = data;
                Toast.makeText(getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), ParentActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
            else {
                editTextTextEmailAddress.setError("Email might be wrong.");
                editTextTextPassword.setError("Email might be wrong.");
                Toast.makeText(getContext(), "Error Authenticating User.", Toast.LENGTH_SHORT).show();
            }

            progressIndicator.setVisibility(View.GONE);
        });
    }
}