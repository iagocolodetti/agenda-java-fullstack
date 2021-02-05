package br.com.iagocolodetti.agenda.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import br.com.iagocolodetti.agenda.R;

public class CustomAlertMessage  {

    private Context context = null;

    public CustomAlertMessage(Context context) {
        this.context = context;
    }

    public void setMessage(TextView textView, String message, String type) {
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
            if (type.equals("success")) {
                textView.setTextColor(context.getResources().getColor(R.color.alert_text_success));
                textView.setBackground(context.getDrawable(R.drawable.alert_success));
            } else if (type.equals("danger")) {
                textView.setTextColor(context.getResources().getColor(R.color.alert_text_danger));
                textView.setBackground(context.getDrawable(R.drawable.alert_danger));
            } else {
                textView.setTextColor(context.getResources().getColor(R.color.black));
                textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
    }
}
