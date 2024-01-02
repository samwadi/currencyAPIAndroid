package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText editTextLoginUsername, editTextLoginPassword;
    private CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLoginUsername = findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        Button buttonGoToRegister = findViewById(R.id.buttonGoToRegister);
        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMeChecked) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");

            editTextLoginUsername.setText(savedUsername);
            editTextLoginPassword.setText(savedPassword);
            checkBoxRememberMe.setChecked(true);
        }
    }

    private void loginUser() {
        String enteredUsername = editTextLoginUsername.getText().toString();
        String enteredPassword = editTextLoginPassword.getText().toString();

        if (isValidLogin(enteredUsername, enteredPassword)) {
            if (checkBoxRememberMe.isChecked()) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("rememberMe", true);
                editor.putString("username", enteredUsername);
                editor.putString("password", enteredPassword);
                editor.apply();
            }

            Intent intent = new Intent(this, ExchangeRateActivity.class);
            startActivity(intent);
        } else {
            showMessage("Invalid username or password");
        }
    }


    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isValidLogin(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
