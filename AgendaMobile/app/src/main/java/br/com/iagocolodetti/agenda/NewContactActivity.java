package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.iagocolodetti.agenda.exception.CustomResponseException;
import br.com.iagocolodetti.agenda.model.Contact;
import br.com.iagocolodetti.agenda.model.Email;
import br.com.iagocolodetti.agenda.model.Phone;
import br.com.iagocolodetti.agenda.model.User;
import br.com.iagocolodetti.agenda.security.AuthUtils;
import br.com.iagocolodetti.agenda.service.ContactService;
import br.com.iagocolodetti.agenda.service.SessionService;
import br.com.iagocolodetti.agenda.util.CustomAlertMessage;
import br.com.iagocolodetti.agenda.util.ListViewAdapter;

/**
 *
 * @author iagocolodetti
 */
public class NewContactActivity extends AppCompatActivity {

    private AuthUtils au = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private Button btSave = null;

    List<Phone> phone = new ArrayList<>();
    List<Email> email = new ArrayList<>();

    private void logout(String message) {
        au.removeAuth();
        Intent intent = new Intent(NewContactActivity.this, LoginActivity.class);
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

    private void setButtonEnabled(boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btSave != null) {
                    btSave.setEnabled(enable);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        setTitle(getResources().getString(R.string.new_contact_title));

        au = new AuthUtils(this);

        cam = new CustomAlertMessage(this);

        TextView tvLogout = findViewById(R.id.new_contact_tvLogout);
        TextInputEditText tietName = findViewById(R.id.new_contact_tietName);
        TextInputEditText tietAlias = findViewById(R.id.new_contact_tietAlias);
        TextInputLayout tilPhone = findViewById(R.id.new_contact_tilPhone);
        TextInputEditText tietPhone = findViewById(R.id.new_contact_tietPhone);
        TextInputLayout tilEmail = findViewById(R.id.new_contact_tilEmail);
        TextInputEditText tietEmail = findViewById(R.id.new_contact_tietEmail);
        tvMessage = findViewById(R.id.new_contact_tvMessage);
        btSave = findViewById(R.id.new_contact_btSave);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(null);
            }
        });

        tilPhone.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phone p = new Phone();
                p.setPhone(tietPhone.getText().toString());
                phone.add(p);
                tietPhone.setText("");
                setPhones();
            }
        });

        tilEmail.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email e = new Email();
                e.setEmail(tietEmail.getText().toString());
                email.add(e);
                tietEmail.setText("");
                setEmails();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tietName.getText().toString();
                String alias = tietAlias.getText().toString();
                if (name.isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.name_empty), "danger");
                } else if (alias.isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.alias_empty), "danger");
                } else if (phone.isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.phones_empty), "danger");
                } else if (email.isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.emails_empty), "danger");
                } else {
                    setButtonEnabled(false);
                    ContactService cs = new ContactService();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cs.create(au.getAuth(), new Contact(name, alias, phone, email));
                                setMessageInsideThread(getResources().getString(R.string.contact_saved_start) + name + getResources().getString(R.string.contact_saved_end), "success");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tietName.setText("");
                                        tietAlias.setText("");
                                        tietPhone.setText("");
                                        tietEmail.setText("");
                                        phone.clear();
                                        setPhones();
                                        email.clear();
                                        setEmails();
                                    }
                                });
                            } catch (CustomResponseException ex) {
                                if (ex.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                    au.removeAuth();
                                    Intent intent = new Intent(NewContactActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("authError", ex.getMessage());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    setMessageInsideThread(getResources().getString(R.string.error_start) + ex.getMessage() + ".", "danger");
                                }
                            } catch (IOException ex) {
                                setMessageInsideThread(getResources().getString(R.string.internal_error), "danger");
                            } finally {
                                setButtonEnabled(true);
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    public void removeItem(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        final String tag = listView.getTag().toString();
        if (tag.equals("lvPhones")) {
            phone.remove(position);
            setPhones();
        } else if (tag.equals("lvEmails")) {
            email.remove(position);
            setEmails();
        }
    }

    private void setPhones() {
        TextView tvPhones = findViewById(R.id.new_contact_tvPhones);
        ListView lvPhones = findViewById(R.id.new_contact_lvPhones);
        if (!phone.isEmpty()) {
            tvPhones.setVisibility(View.VISIBLE);
            ArrayAdapter<Phone> adapter = new ArrayAdapter<Phone>(NewContactActivity.this, R.layout.list_textview_item_with_button, R.id.tvListViewItemWithButton, phone) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvPhone = view.findViewById(R.id.tvListViewItemWithButton);
                    tvPhone.setText(phone.get(position).getPhone());
                    return view;
                }
            };
            lvPhones.setAdapter(adapter);
            lvPhones.setVisibility(View.VISIBLE);
            ListViewAdapter.setListViewHeightBasedOnChildren(lvPhones);
        } else {
            tvPhones.setVisibility(View.GONE);
            lvPhones.setVisibility(View.GONE);
        }
    }

    private void setEmails() {
        TextView tvEmails = findViewById(R.id.new_contact_tvEmails);
        ListView lvEmails = findViewById(R.id.new_contact_lvEmails);
        if (!email.isEmpty()) {
            tvEmails.setVisibility(View.VISIBLE);
            ArrayAdapter<Email> adapter = new ArrayAdapter<Email>(NewContactActivity.this, R.layout.list_textview_item_with_button, R.id.tvListViewItemWithButton, email) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvEmail = view.findViewById(R.id.tvListViewItemWithButton);
                    tvEmail.setText(email.get(position).getEmail());
                    return view;
                }
            };
            lvEmails.setAdapter(adapter);
            lvEmails.setVisibility(View.VISIBLE);
            ListViewAdapter.setListViewHeightBasedOnChildren(lvEmails);
        } else {
            tvEmails.setVisibility(View.GONE);
            lvEmails.setVisibility(View.GONE);
        }
    }
}