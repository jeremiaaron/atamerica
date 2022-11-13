package com.example.atamerica.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atamerica.R;
import com.example.atamerica.databinding.FragmentLoginBinding;
import com.example.atamerica.ui.signup.SignUpFragment;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private TextView labelForgot, labelSignUp;
    private EditText editTextTextEmailAddress, editTextTextPassword;
    private CheckBox checkboxRememberMe;
    private Button   buttonSignIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Bind binding
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        // Bind views
        labelForgot = (TextView) rootView.findViewById(R.id.labelForgot);
        labelSignUp = (TextView) rootView.findViewById(R.id.labelSignUp);
        editTextTextEmailAddress = (EditText) rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) rootView.findViewById(R.id.editTextTextPassword);
        checkboxRememberMe = (CheckBox) rootView.findViewById(R.id.checkboxRememberMe);
        buttonSignIn = (Button) rootView.findViewById(R.id.buttonSignIn);

        labelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.authenticateContainer, new SignUpFragment(), null);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }
}