package com.exemple.eatmore;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class EditDialog extends Dialog implements View.OnClickListener {

    private final Activity activity;
    public Button confirm, cancel;
    private EditText newData, currentPassword;

    public EditDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        confirm = findViewById(R.id.confirm_dialog_button);
        cancel = findViewById(R.id.close_dialog_button);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
        newData = findViewById(R.id.new_data);
        currentPassword = findViewById(R.id.current_password);

        String editable = activity.getIntent().getExtras().getString("editable");

        switch (editable) {
            case "email":
                newData.setHint(R.string.enter_email_or_username);
                newData.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "password":
                newData.setHint(R.string.password);
                newData.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case "phone":
                newData.setHint(R.string.phone_number);
                newData.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_dialog_button:
                //apply changes
                Snackbar.make(v, "applying changes", BaseTransientBottomBar.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.close_dialog_button:
                dismiss();
                break;

        }
    }
}
