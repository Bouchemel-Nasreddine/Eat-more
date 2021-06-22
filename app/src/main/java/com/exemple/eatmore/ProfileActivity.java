package com.exemple.eatmore;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {

    private MaterialButton back, editEmail, editPassword, editPhone, editPicture;
    private EditText email, password, phone, user_ID;
    private User user;
    private ShapeableImageView picture;
    private TextView verifyEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUtils.reloadUser();
        FirebaseUtils.setUser();

        user = FirebaseUtils.user;
        user.setVerifiedEmail(FirebaseUtils.checkEmailVerification());

        back = findViewById(R.id.back_button);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        editPhone = findViewById(R.id.edit_phone);
        email = findViewById(R.id.profile_email_edit_text);
        password = findViewById(R.id.profile_password_edit_text);
        phone = findViewById(R.id.profile_phone_edit_text);
        user_ID = findViewById(R.id.profile_ID_edit_text);
        picture = findViewById(R.id.profile_picture);
        verifyEmail = findViewById(R.id.verify_email);
        editPicture = findViewById(R.id.edit_picture);

        updateUI();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("email");
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("password");
            }
        });

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editField("phone");
            }
        });

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.sendVerificationEmail();
                Toast.makeText(getApplicationContext(), "email sent", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(getApplicationContext())
                        .load(data.getData())
                        .centerCrop()
                        .signature(new ObjectKey(2000))
                        .into(picture);

                FirebaseUtils.uploadProfilePicture(data.getData());

            }
        }
    }

    public void editField(String str) {
        EditDialog dialog = new EditDialog(ProfileActivity.this);
        dialog.show();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double w = metrics.widthPixels * 0.8;
        double h = metrics.heightPixels * 0.6;
        dialog.getWindow().setLayout((int) w, (int) h);
    }

    private void updateUI() {
        email.setText(user.getEmail());
        phone.setText(formatPhone(user.getPhone()));
        user_ID.setText(String.valueOf(user.getUser_ID()));
        Glide.with(getApplicationContext())
                .load(user.getPicture())
                .placeholder(R.drawable.loading)
                .centerCrop()
                .signature(new ObjectKey(2000))
                .into(picture);

        if (!user.isVerifiedEmail()) {
            email.setError("email is not verified");
            editEmail.setVisibility(View.GONE);
            editPhone.setVisibility(View.GONE);
            editPassword.setVisibility(View.GONE);
        } else {
            verifyEmail.setVisibility(View.GONE);
        }


    }

    private String formatPhone(String number) {
        return number.substring(0, 3) + " " + number.substring(3, 6) + " " + number.substring(6);
    }

}