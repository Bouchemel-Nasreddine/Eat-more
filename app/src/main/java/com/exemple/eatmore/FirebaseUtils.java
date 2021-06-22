package com.exemple.eatmore;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseUtils {

    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    protected static User user;
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();


    public static void setUser() {
        user = new User(fAuth.getCurrentUser().getEmail(), 22000, fAuth.getCurrentUser().getPhotoUrl(), "0000000000");
        /**StorageReference fileRef = storageReference.child(fAuth.getCurrentUser().getUid() + ".jpg");
         fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override public void onSuccess(Uri uri) {
        Log.d("photo downloading", "onSuccess: photo uri downloaded successfully");
        user = new User(fAuth.getCurrentUser().getEmail(), 22000, uri, "0000000000");
        }
        }).addOnFailureListener(new OnFailureListener() {
        @Override public void onFailure(@NonNull Exception e) {
        Log.d("photo downloading", "onSuccess: photo uri downloaded unsuccessfully" + e.getMessage());
        }
        });
         **/
    }

    public static boolean isCurrentUserConnected() {
        return fAuth.getCurrentUser() != null;
    }

    public static void sendVerificationEmail() {
        fAuth.getCurrentUser().sendEmailVerification();
    }

    public static boolean checkEmailVerification() {
        reloadUser();
        return fAuth.getCurrentUser().isEmailVerified();
    }

    public static void reloadUser() {
        fAuth = FirebaseAuth.getInstance();
        fAuth.getCurrentUser().reload();
    }

    public static void logIn(String email, String password) {
        if (fAuth.getCurrentUser() != null) return;
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("connexion", "onComplete: success");
                } else {
                    Log.d("connexion", "onComplete: failed " + task.getException());
                }
            }
        });
        while (fAuth.getCurrentUser() == null) ;
        user = new User(fAuth.getCurrentUser().getEmail(), 22000, null, "0000000000");
        int i = 5;
    }

    public static void signUp(Activity activity, String email, String password) {

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("creation", "onComplete: success");
                } else {
                    Log.d("creation", "onComplete: failed: " + task.getException());
                }
            }
        });

        while (fAuth.getCurrentUser() == null) ;

        user = new User(fAuth.getCurrentUser().getEmail(), 22000, null, "0000000000");
        sendVerificationEmail();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void uploadProfilePicture(Uri imageUri) {
        StorageReference fileRef = storageReference.child(fAuth.getCurrentUser().getUid() + ".jpg");
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = fAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getEmail())
                            .setPhotoUri(imageUri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("updating user photo uri", "User profile updated.");
                                    }
                                }
                            });
                    Log.d("photo upload", "onComplete: photo uploaded successfully");

                } else {
                    Log.d("photo upload", "onComplete: error uploading photo" + task.getException());
                }
            }
        });
    }
}

