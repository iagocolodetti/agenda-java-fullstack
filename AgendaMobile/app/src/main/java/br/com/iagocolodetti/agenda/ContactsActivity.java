package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.ContactService;
import br.com.iagocolodetti.agenda.util.CustomAlertMessage;

/**
 *
 * @author iagocolodetti
 */
public class ContactsActivity extends AppCompatActivity {

    private AuthUtils au = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private SwipeRefreshLayout pullToRefresh = null;

    private List<Contact> contacts = null;

    private void logout(String message) {
        au.removeAuth();
        Intent intent = new Intent(ContactsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("authError", message);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle(getResources().getString(R.string.contacts_title));

        au = new AuthUtils(this);

        cam = new CustomAlertMessage(this);

        tvMessage = findViewById(R.id.contacts_tvMessage);
        TextView tvLogout = findViewById(R.id.contacts_tvLogout);
        FloatingActionButton btNewContact = findViewById(R.id.contacts_btNewContact);
        pullToRefresh = findViewById(R.id.contacts_pullToRefresh);

        String contactUpdated = getIntent().getStringExtra("contactUpdated");
        String contactDeleted = getIntent().getStringExtra("contactDeleted");
        if (contactUpdated != null && !contactUpdated.isEmpty()) {
            cam.setMessage(tvMessage, contactUpdated, "success");
            getIntent().removeExtra("contactUpdated");
        } else if (contactDeleted != null && !contactDeleted.isEmpty()) {
            cam.setMessage(tvMessage, contactDeleted, "danger");
            getIntent().removeExtra("contactDeleted");
        }

        tvLogout.setOnClickListener(view -> logout(null));

        pullToRefresh.setOnRefreshListener(() -> {
            tvMessage.setVisibility(View.GONE);
            //new setContacts().execute();
            setContacts();
        });

        btNewContact.setOnClickListener(view -> {
            Intent intent = new Intent(ContactsActivity.this, NewContactActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //new setContacts().execute();
        setContacts();
    }

    private void setContacts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            String tempError = null;
            int tempErrorCode = 0;
            List<Contact> tempContacts;
            try {
                tempContacts = new ContactService().read(au.getAuth());
            } catch (CustomResponseException ex) {
                tempError = ex.getMessage();
                tempErrorCode = ex.getStatus();
                tempContacts = null;
            } catch (IOException ex) {
                tempError = getResources().getString(R.string.contacts_default_error);
                tempContacts = null;
            }
            String error = tempError;
            int errorCode = tempErrorCode;
            List<Contact> _contacts = tempContacts;
            handler.post(() -> {
                if (error != null && !error.isEmpty()) {
                    if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        logout(error);
                    } else {
                        cam.setMessage(tvMessage, getResources().getString(R.string.error_start) + error + ".", "danger");
                    }
                } else if (_contacts != null && !_contacts.isEmpty()) {
                    _contacts.sort(Comparator.comparing(c -> c.getName().toUpperCase()));
                    contacts = _contacts;
                    ArrayAdapter<Contact> adapter = new ArrayAdapter<>(ContactsActivity.this, R.layout.list_textview_contact, _contacts) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView tvContact = view.findViewById(R.id.tvListViewContact);
                            String text = "<normal><font color=\"#444\">" + _contacts.get(position).getName() + "</font></normal><br/>"
                                    + "<small><font color=\"#444\">" + _contacts.get(position).getAlias() + "</font></small><br/>";
                            if (position > 0) {
                                text = "<br/>" + text;
                            }
                            tvContact.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                            return view;
                        }
                    };
                    ListView lvContacts = findViewById(R.id.contacts_lvContacts);
                    lvContacts.setAdapter(adapter);
                    lvContacts.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(ContactsActivity.this, ContactActivity.class);
                        intent.putExtra("contact", contacts.get(position).toJson());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                } else {
                    cam.setMessage(tvMessage, getResources().getString(R.string.contacts_empty), "danger");
                }
                pullToRefresh.setRefreshing(false);
            });
        });
    }
}