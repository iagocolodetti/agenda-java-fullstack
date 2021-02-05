package br.com.iagocolodetti.agenda.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 * @author iagocolodetti
 */
public class AuthUtils {

    private SharedPreferences preferences;

    public AuthUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAuth(String auth) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("agendaApiAuth", auth);
        editor.apply();
    }

    public String getAuth() {
        return preferences.getString("agendaApiAuth", "");
    }

    public void removeAuth() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("agendaApiAuth");
        editor.apply();
    }
}
