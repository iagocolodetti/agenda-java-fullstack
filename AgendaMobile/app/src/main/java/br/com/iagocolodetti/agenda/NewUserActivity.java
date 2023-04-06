package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.service.UserService;
import br.com.iagocolodetti.agenda.util.CustomAlertMessage;

/**
 *
 * @author iagocolodetti
 */
public class NewUserActivity extends AppCompatActivity {

    private Button btNew = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private void setMessageInsideThread(String message, String type) {
        runOnUiThread(() -> cam.setMessage(tvMessage, message, type));
    }

    private void setButtonEnabled(boolean enable) {
        runOnUiThread(() -> {
            if (btNew != null) {
                btNew.setEnabled(enable);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        setTitle(getResources().getString(R.string.new_user_title));

        cam = new CustomAlertMessage(this);

        TextInputEditText tietUsername = findViewById(R.id.new_user_tietUsername);
        TextInputEditText tietPassword = findViewById(R.id.new_user_tietPassword);
        TextInputEditText tietConfirmPassword = findViewById(R.id.new_user_tietConfirmPassword);
        btNew = findViewById(R.id.new_user_btNew);
        tvMessage = findViewById(R.id.new_user_tvMessage);

        btNew.setOnClickListener(view -> {
            String username = tietUsername.getText().toString();
            String password = tietPassword.getText().toString();
            String confirmPassword = tietConfirmPassword.getText().toString();
            if (username.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.username_empty), "danger");
            } else if (username.length() < getResources().getInteger(R.integer.username_min_length) || username.length() > getResources().getInteger(R.integer.username_max_length)) {
                cam.setMessage(tvMessage, getResources().getString(R.string.username_min_max_length, getResources().getInteger(R.integer.username_min_length), getResources().getInteger(R.integer.username_max_length)), "danger");
            } else if (password.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.password_empty), "danger");
            } else if (password.length() < getResources().getInteger(R.integer.password_min_length) || password.length() > getResources().getInteger(R.integer.password_max_length)) {
                cam.setMessage(tvMessage, getResources().getString(R.string.password_min_max_length, getResources().getInteger(R.integer.password_min_length), getResources().getInteger(R.integer.password_max_length)), "danger");
            } else if (confirmPassword.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.confirm_password_empty), "danger");
            } else if (!password.equals(confirmPassword)) {
                cam.setMessage(tvMessage, getResources().getString(R.string.passwords_not_equals), "danger");
            } else {
                setButtonEnabled(false);
                UserService us = new UserService();
                Thread thread = new Thread(() -> {
                    try {
                        us.create(new User(username, password));
                    } catch (CustomResponseException ex) {
                        setMessageInsideThread(getResources().getString(R.string.error_start) + ex.getMessage() + ".", "danger");
                    } catch (IOException ex) {
                        setMessageInsideThread(getResources().getString(R.string.internal_error), "danger");
                    } finally {
                        setButtonEnabled(true);
                    }
                });
                thread.start();
            }
        });
    }
}