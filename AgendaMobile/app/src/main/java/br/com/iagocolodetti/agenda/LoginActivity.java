package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.SessionService;
import br.com.iagocolodetti.agenda.util.CustomAlertMessage;

/**
 * @author iagocolodetti
 */
public class LoginActivity extends AppCompatActivity {

    private AuthUtils au = null;

    TextView tvNewUser = null;
    private Button btLogin = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private void setMessageInsideThread(String message, String type) {
        runOnUiThread(() -> cam.setMessage(tvMessage, message, type));
    }

    private void setButtonsEnabled(boolean enable) {
        runOnUiThread(() -> {
            if (tvNewUser != null) {
                tvNewUser.setEnabled(enable);
            }
            if (btLogin != null) {
                btLogin.setEnabled(enable);
            }
        });
    }

    private void goToContacts() {
        Intent intent = new Intent(LoginActivity.this, ContactsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getResources().getString(R.string.login_title));

        au = new AuthUtils(this);

        cam = new CustomAlertMessage(this);

        TextInputEditText tietUsername = findViewById(R.id.login_tietUsername);
        TextInputEditText tietPassword = findViewById(R.id.login_tietPassword);
        tvNewUser = findViewById(R.id.login_tvNewUser);
        btLogin = findViewById(R.id.login_btLogin);
        tvMessage = findViewById(R.id.login_tvMessage);

        String authorization = au.getAuth();
        if (authorization != null && !authorization.isEmpty()) {
            goToContacts();
        } else {
            String error = getIntent().getStringExtra("authError");
            if (error != null && !error.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.error_start) + error + ".", "danger");
            }
        }

        tvNewUser.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btLogin.setOnClickListener(view -> {
            String username = tietUsername.getText().toString();
            String password = tietPassword.getText().toString();
            if (username.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.username_empty), "danger");
            } else if (password.isEmpty()) {
                cam.setMessage(tvMessage, getResources().getString(R.string.password_empty), "danger");
            } else {
                setButtonsEnabled(false);
                SessionService ss = new SessionService();
                Thread thread = new Thread(() -> {
                    try {
                        String authorization1 = ss.create(new User(username, password));
                        au.setAuth(authorization1);
                        goToContacts();
                    } catch (CustomResponseException ex) {
                        setMessageInsideThread(getResources().getString(R.string.error_start) + ex.getMessage() + ".", "danger");
                    } catch (IOException ex) {
                        setMessageInsideThread(getResources().getString(R.string.internal_error), "danger");
                    } finally {
                        setButtonsEnabled(true);
                    }
                });
                thread.start();
            }
        });
    }
}
