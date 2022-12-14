package com.example.atamerica.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentSignUpBinding;
import com.example.atamerica.dbhandler.DataHelper;
import com.example.atamerica.models.AppUserModel;
import com.example.atamerica.taskhandler.TaskRunner;
import com.example.atamerica.ui.login.LoginFragment;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.Callable;

import JavaLibrary.AESUtil;

public class SignUpFragment extends Fragment {

    private class RegisterTask implements Callable<String> {
        @Override
        public String call() {
            try {
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
                        return "Success";
                    }
                    else {
                        return "Error";
                    }
                }
                else {
                    return "Exists";
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }

    private EditText                    editTextTextFullName, editTextTextEmailAddress, editTextTextPhone, editTextTextPassword;
    private String                      fullName, email, phoneNumber, password;
    private CircularProgressIndicator   progressIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind binding
        com.example.atamerica.databinding.FragmentSignUpBinding binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Bind views
        TextView labelSignUp = (TextView) rootView.findViewById(R.id.labelSignUp);
        editTextTextFullName        = (EditText) rootView.findViewById(R.id.editTextTextFullName);
        editTextTextEmailAddress    = (EditText) rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone           = (EditText) rootView.findViewById(R.id.editTextTextPhone);
        editTextTextPassword        = (EditText) rootView.findViewById(R.id.editTextTextPassword);
        Button buttonSignUp         = (Button) rootView.findViewById(R.id.buttonSignUp);
        progressIndicator           = (CircularProgressIndicator) requireActivity().findViewById(R.id.progressIndicator);

        buttonSignUp.setOnClickListener(view -> {
            progressIndicator.setVisibility(View.VISIBLE);
            SignUp();
        });

        labelSignUp.setOnClickListener(view -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
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

        if (isRequired) {
            progressIndicator.setVisibility(View.GONE);
            return;
        }

        this.fullName     = editTextTextFullName.getText().toString();
        this.email        = editTextTextEmailAddress.getText().toString();
        this.phoneNumber  = editTextTextPhone.getText().toString();
        this.password     = editTextTextPassword.getText().toString();

        new TaskRunner().executeAsyncSingle(new RegisterTask(), (data) -> {
            switch (data) {
                case "Success":
                    Toast.makeText(getContext(), "Successfully created user. Please login again.", Toast.LENGTH_SHORT).show();
                    // Call login again
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    transaction.replace(R.id.authenticateContainer, new LoginFragment(), null);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    break;
                case "Exists":
                    Toast.makeText(getContext(), "Email already exists as user.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getContext(), "Error processing request", Toast.LENGTH_SHORT).show();
                    break;

            }

            progressIndicator.setVisibility(View.GONE);
        });
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