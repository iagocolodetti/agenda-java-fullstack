package br.com.iagocolodetti.agenda.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import br.com.iagocolodetti.agenda.R;

public class CustomAlertMessage  {

    private final Context context;

    public CustomAlertMessage(Context context) {
        this.context = context;
    }

    public void setMessage(TextView textView, String message, String type) {
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
            if (type.equals("success")) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.alert_text_success));
                textView.setBackground(AppCompatResources.getDrawable(context, R.drawable.alert_success));
            } else if (type.equals("danger")) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.alert_text_danger));
                textView.setBackground(AppCompatResources.getDrawable(context, R.drawable.alert_danger));
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
        }
    }
}
