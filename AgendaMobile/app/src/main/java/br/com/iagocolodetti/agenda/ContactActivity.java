package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.ContactService;
import br.com.iagocolodetti.agenda.util.CustomAlertMessage;
import br.com.iagocolodetti.agenda.util.ListViewAdapter;

/**
 *
 * @author iagocolodetti
 */
public class ContactActivity extends AppCompatActivity {

    private AuthUtils au = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private Button btUpdate, btDelete = null;

    private void logout(String message) {
        au.removeAuth();
        Intent intent = new Intent(ContactActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("authError", message);
        startActivity(intent);
        finish();
    }

    private void setMessageInsideThread(String message, String type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cam.setMessage(tvMessage, message, type);
            }
        });
    }

    private void setButtonsEnabled(boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btUpdate != null) {
                    btUpdate.setEnabled(enable);
                }
                if (btDelete != null) {
                    btDelete.setEnabled(enable);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle(getResources().getString(R.string.contact_title));

        au = new AuthUtils(this);

        cam = new CustomAlertMessage(this);

        tvMessage = findViewById(R.id.contact_tvMessage);
        TextView tvLogout = findViewById(R.id.contact_tvLogout);
        TextView tvName = findViewById(R.id.contact_tvName);
        TextView tvAlias = findViewById(R.id.contact_tvAlias);
        btUpdate = findViewById(R.id.contact_btUpdate);
        btDelete = findViewById(R.id.contact_btDelete);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(null);
            }
        });

        try {
            Contact contact = new Contact(getIntent().getStringExtra("contact"));

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ContactActivity.this, UpdateContactActivity.class);
                    intent.putExtra("contact", contact.toJson());
                    startActivity(intent);
                }
            });

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactActivity.this);
                    builder.setTitle(getResources().getString(R.string.delete_contact_title));
                    builder.setMessage(getResources().getString(R.string.delete_contact_message))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setButtonsEnabled(false);
                                            try {
                                                new ContactService().destroy(au.getAuth(), contact.getId());
                                                Intent intent = new Intent(ContactActivity.this, ContactsActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("contactDeleted", getResources().getString(R.string.contact_deleted_start) + contact.getName() + getResources().getString(R.string.contact_deleted_end));
                                                startActivity(intent);
                                                finish();
                                            } catch (CustomResponseException ex) {
                                                if (ex.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                                    logout(ex.getMessage());
                                                } else {
                                                    setMessageInsideThread(getResources().getString(R.string.error_start) + ex.getMessage() + ".", "danger");
                                                }
                                            } catch (IOException ex) {
                                                setMessageInsideThread(getResources().getString(R.string.internal_error), "danger");
                                            } finally {
                                                setButtonsEnabled(true);
                                            }
                                        }
                                    });
                                    thread.start();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
                }
            });

            tvName.setText(contact.getName());
            tvAlias.setText(contact.getAlias());

            if (contact.getPhone() != null && !contact.getPhone().isEmpty()) {
                TextView tvPhones = findViewById(R.id.contact_tvPhones);
                tvPhones.setVisibility(View.VISIBLE);
                ArrayAdapter<Phone> adapter = new ArrayAdapter<Phone>(ContactActivity.this, R.layout.list_textview_item, contact.getPhone()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tvPhone = view.findViewById(R.id.tvListViewItem);
                        tvPhone.setText(contact.getPhone().get(position).getPhone());
                        return view;
                    }
                };
                ListView lvPhones = findViewById(R.id.contact_lvPhones);
                lvPhones.setAdapter(adapter);
                lvPhones.setVisibility(View.VISIBLE);
                ListViewAdapter.setListViewHeightBasedOnChildren(lvPhones);
            }

            if (contact.getEmail() != null && !contact.getEmail().isEmpty()) {
                TextView tvEmails = findViewById(R.id.contact_tvEmails);
                tvEmails.setVisibility(View.VISIBLE);
                ArrayAdapter<Email> adapter = new ArrayAdapter<Email>(ContactActivity.this, R.layout.list_textview_item, contact.getEmail()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tvEmail = view.findViewById(R.id.tvListViewItem);
                        tvEmail.setText(contact.getEmail().get(position).getEmail());
                        return view;
                    }
                };
                ListView lvEmails = findViewById(R.id.contact_lvEmails);
                lvEmails.setAdapter(adapter);
                lvEmails.setVisibility(View.VISIBLE);
                ListViewAdapter.setListViewHeightBasedOnChildren(lvEmails);
            }
        } catch (IllegalArgumentException ex) {
            tvName.setVisibility(View.GONE);
            tvAlias.setVisibility(View.GONE);
            btUpdate.setVisibility(View.GONE);
            btDelete.setVisibility(View.GONE);
            cam.setMessage(tvMessage, getResources().getString(R.string.internal_error), "danger");
        }
    }
}