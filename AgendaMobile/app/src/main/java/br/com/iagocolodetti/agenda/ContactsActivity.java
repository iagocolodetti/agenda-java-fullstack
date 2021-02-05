package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

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

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(null);
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvMessage.setVisibility(View.GONE);
                new setContacts().execute();
            }
        });

        btNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, NewContactActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new setContacts().execute();
    }

    private class setContacts extends AsyncTask<Void, Void, List<Contact>> {

        private String error = null;
        private int errorCode = 0;

        protected void onPreExecute() {
        }

        protected List<Contact> doInBackground(Void... nothing) {
            try {
                return new ContactService().read(au.getAuth());
            } catch (CustomResponseException ex) {
                error = ex.getMessage();
                errorCode = ex.getStatus();
                return null;
            } catch (IOException ex) {
                error = getResources().getString(R.string.contacts_default_error);
                return null;
            }
        }

        protected void onProgressUpdate(Void... nothing) {
        }

        protected void onPostExecute(List<Contact> _contacts) {
            if (error != null && !error.isEmpty()) {
                if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    logout(error);
                } else {
                    cam.setMessage(tvMessage, getResources().getString(R.string.error_start) + error + ".", "danger");
                }
            } else if (_contacts != null && !_contacts.isEmpty()) {
                Collections.sort(_contacts, (c1, c2) -> c1.getName().toUpperCase().compareTo(c2.getName().toUpperCase()));
                contacts = _contacts;
                ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(ContactsActivity.this, R.layout.list_textview_contact, _contacts) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tvContact = view.findViewById(R.id.tvListViewContact);
                        String text = "<normal><font color=\"#444\">" + _contacts.get(position).getName() + "</font></normal><br/>"
                                + "<small><font color=\"#444\">" + _contacts.get(position).getAlias() + "</font></small><br/>";
                        if (position > 0) {
                            text = "<br/>" + text;
                        }
                        tvContact.setText(Html.fromHtml(text));
                        return view;
                    }
                };
                ListView lvContacts = findViewById(R.id.contacts_lvContacts);
                lvContacts.setAdapter(adapter);
                lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ContactsActivity.this, ContactActivity.class);
                        intent.putExtra("contact", contacts.get(position).toJson());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            } else {
                cam.setMessage(tvMessage, getResources().getString(R.string.contacts_empty), "danger");
            }
            pullToRefresh.setRefreshing(false);
        }
    }
}