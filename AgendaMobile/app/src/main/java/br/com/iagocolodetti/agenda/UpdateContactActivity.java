package br.com.iagocolodetti.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
public class UpdateContactActivity extends AppCompatActivity {

    private AuthUtils au = null;

    private CustomAlertMessage cam = null;
    private TextView tvMessage = null;

    private Button btUpdate = null;

    private Contact contact = null;

    private int lastPhoneID = 0;
    private int lastEmailID = 0;

    private ArrayAdapter<Phone> phoneAdapter;
    private ArrayAdapter<Email> emailAdapter;

    private void logout(String message) {
        au.removeAuth();
        Intent intent = new Intent(UpdateContactActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("authError", message);
        startActivity(intent);
        finish();
    }

    private void setMessageInsideThread(String message, String type) {
        runOnUiThread(() -> cam.setMessage(tvMessage, message, type));
    }

    private void setButtonEnabled(boolean enable) {
        runOnUiThread(() -> {
            if (btUpdate != null) {
                btUpdate.setEnabled(enable);
            }
        });
    }

    private List<Phone> getPhonesIgnoreDeleted() {
        List<Phone> phones = new ArrayList<>();
        for (Phone phone : contact.getPhone()) {
            if (!phone.isDeleted()) {
                phones.add(phone);
            }
        }
        return phones;
    }

    private int getIndexOfPhoneID(int id) {
        for (int i = 0; i < contact.getPhone().size(); i++) {
            if (contact.getPhone().get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private List<Email> getEmailsIgnoreDeleted() {
        List<Email> emails = new ArrayList<>();
        for (Email email : contact.getEmail()) {
            if (!email.isDeleted()) {
                emails.add(email);
            }
        }
        return emails;
    }

    private int getIndexOfEmailID(int id) {
        for (int i = 0; i < contact.getEmail().size(); i++) {
            if (contact.getEmail().get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        setTitle(getResources().getString(R.string.update_contact_title));

        au = new AuthUtils(this);

        cam = new CustomAlertMessage(this);

        TextView tvLogout = findViewById(R.id.update_contact_tvLogout);
        TextInputEditText tietName = findViewById(R.id.update_contact_tietName);
        TextInputEditText tietAlias = findViewById(R.id.update_contact_tietAlias);
        TextInputLayout tilPhone = findViewById(R.id.update_contact_tilPhone);
        TextInputEditText tietPhone = findViewById(R.id.update_contact_tietPhone);
        TextInputLayout tilEmail = findViewById(R.id.update_contact_tilEmail);
        TextInputEditText tietEmail = findViewById(R.id.update_contact_tietEmail);
        tvMessage = findViewById(R.id.update_contact_tvMessage);
        btUpdate = findViewById(R.id.update_contact_btUpdate);

        tvLogout.setOnClickListener(view -> logout(null));

        try {
            contact = new Contact(getIntent().getStringExtra("contact"));
            tietName.setText(contact.getName());
            tietAlias.setText(contact.getAlias());
            setPhones();
            setEmails();

            tilPhone.setEndIconOnClickListener(view -> {
                String _phone = tietPhone.getText().toString();
                if (_phone.length() >= getResources().getInteger(R.integer.phone_min_length) && _phone.length() <= getResources().getInteger(R.integer.phone_max_length)) {
                    Phone p = new Phone();
                    p.setId(--lastPhoneID);
                    p.setPhone(_phone);
                    contact.getPhone().add(p);
                    tietPhone.setText("");
                    setPhones();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
                }
            });

            tilEmail.setEndIconOnClickListener(view -> {
                String _email = tietEmail.getText().toString();
                String[] _emailArray = _email.split("@");
                if (Pattern.matches(getResources().getString(R.string.email_pattern), _email) && _emailArray[0].length() <= getResources().getInteger(R.integer.email_name_max_length) && _emailArray[1].length() <= getResources().getInteger(R.integer.email_address_max_length)) {
                    Email e = new Email();
                    e.setId(--lastEmailID);
                    e.setEmail(_email);
                    contact.getEmail().add(e);
                    tietEmail.setText("");
                    setEmails();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                }
            });

            btUpdate.setOnClickListener(view -> {
                contact.setName(tietName.getText().toString());
                contact.setAlias(tietAlias.getText().toString());
                if (contact.getName().isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.name_empty), "danger");
                } else if (contact.getName().length() < getResources().getInteger(R.integer.name_min_length) || contact.getName().length() > getResources().getInteger(R.integer.name_max_length)) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.name_min_max_length, getResources().getInteger(R.integer.name_min_length), getResources().getInteger(R.integer.name_max_length)), "danger");
                } else if (contact.getAlias().isEmpty()) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.alias_empty), "danger");
                } else if (contact.getAlias().length() < getResources().getInteger(R.integer.alias_min_length) || contact.getAlias().length() > getResources().getInteger(R.integer.alias_max_length)) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.alias_min_max_length, getResources().getInteger(R.integer.alias_min_length), getResources().getInteger(R.integer.alias_max_length)), "danger");
                } else if (contact.getPhone().isEmpty() || !contact.getPhone().stream().anyMatch(p -> !p.isDeleted())) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.phones_empty), "danger");
                } else if (contact.getEmail().isEmpty() || !contact.getEmail().stream().anyMatch(e -> !e.isDeleted())) {
                    cam.setMessage(tvMessage, getResources().getString(R.string.emails_empty), "danger");
                } else {
                    ContactService cs = new ContactService();
                    Thread thread = new Thread(() -> {
                        setButtonEnabled(false);
                        try {
                            for (int i = 0; i < contact.getPhone().size(); i++) {
                                if (contact.getPhone().get(i).getId() < 0) {
                                    contact.getPhone().get(i).setId(null);
                                }
                            }
                            for (int i = 0; i < contact.getEmail().size(); i++) {
                                if (contact.getEmail().get(i).getId() < 0) {
                                    contact.getEmail().get(i).setId(null);
                                }
                            }
                            cs.update(au.getAuth(), contact);
                            Intent intent = new Intent(UpdateContactActivity.this, ContactsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("contactUpdated", getResources().getString(R.string.contact_updated, contact.getName()));
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
                            setButtonEnabled(true);
                        }
                    });
                    thread.start();
                }
            });
        } catch (IllegalArgumentException ex) {
            cam.setMessage(tvMessage, getResources().getString(R.string.internal_error), "danger");
        }
    }

    public void removeItem(View view) {
        View parentRow = (View) view.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        final String tag = listView.getTag().toString();
        if (tag.equals("lvPhones")) {
            int index = getIndexOfPhoneID(getPhonesIgnoreDeleted().get(position).getId());
            if (contact.getPhone().get(index).getId() < 0) {
                contact.getPhone().remove(index);
            } else {
                contact.getPhone().get(index).setDeleted(true);
            }
            setPhones();
        } else if (tag.equals("lvEmails")) {
            int index = getIndexOfEmailID(getEmailsIgnoreDeleted().get(position).getId());
            if (contact.getEmail().get(index).getId() < 0) {
                contact.getEmail().remove(index);
            } else {
                contact.getEmail().get(index).setDeleted(true);
            }
            setEmails();
        }
    }

    private void setPhones() {
        if (phoneAdapter != null) {
            phoneAdapter.notifyDataSetChanged();
        }
        TextView tvPhones = findViewById(R.id.update_contact_tvPhones);
        ListView lvPhones = findViewById(R.id.update_contact_lvPhones);
        if (!contact.getPhone().isEmpty()) {
            tvPhones.setVisibility(View.VISIBLE);
            List<Phone> phones = getPhonesIgnoreDeleted();
            phoneAdapter = new ArrayAdapter<>(UpdateContactActivity.this, R.layout.list_textview_item_with_button, R.id.tvListViewItemWithButton, phones) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvPhone = view.findViewById(R.id.tvListViewItemWithButton);
                    tvPhone.setText(phones.get(position).getPhone());
                    return view;
                }
            };
            lvPhones.setAdapter(phoneAdapter);
            lvPhones.setVisibility(View.VISIBLE);
            ListViewAdapter.setListViewHeightBasedOnChildren(lvPhones);
        } else {
            tvPhones.setVisibility(View.GONE);
            lvPhones.setVisibility(View.GONE);
        }
    }

    private void setEmails() {
        if (emailAdapter != null) {
            emailAdapter.notifyDataSetChanged();
        }
        TextView tvEmails = findViewById(R.id.update_contact_tvEmails);
        ListView lvEmails = findViewById(R.id.update_contact_lvEmails);
        if (!contact.getEmail().isEmpty()) {
            tvEmails.setVisibility(View.VISIBLE);
            List<Email> emails = getEmailsIgnoreDeleted();
            emailAdapter = new ArrayAdapter<>(UpdateContactActivity.this, R.layout.list_textview_item_with_button, R.id.tvListViewItemWithButton, emails) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvEmail = view.findViewById(R.id.tvListViewItemWithButton);
                    tvEmail.setText(emails.get(position).getEmail());
                    return view;
                }
            };
            lvEmails.setAdapter(emailAdapter);
            lvEmails.setVisibility(View.VISIBLE);
            ListViewAdapter.setListViewHeightBasedOnChildren(lvEmails);
        } else {
            tvEmails.setVisibility(View.GONE);
            lvEmails.setVisibility(View.GONE);
        }
    }
}