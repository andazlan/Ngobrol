package com.ngobrol.Member;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ngobrol.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {
    @Bind(R.id.txt_name) TextInputEditText mName;
    @Bind(R.id.txt_email) TextInputEditText mEmail;
    @Bind(R.id.txt_password) EditText mPassword;
    @Bind(R.id.btn_register) Button mRegister;
    @Bind(R.id.login_form) View form;
    @Bind(R.id.login_progress) View progress;

    public static String TAG = "FirebaseRegistration";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isParamValid()){
                    doRegister();
                }
            }
        });
    }

    private void doRegister() {
        showProgress(true);
        mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.getException() == null){
                            updateUserProfile();
                        }
                        else{
                            Log.d(TAG, task.getException().getMessage());
                            Toast.makeText(RegistrationActivity.this, "Register tidak berhasil", Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                });
    }

    private void updateUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName.getText().toString().trim())
                .build();
        user.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete() && task.getException() == null){
                    finish();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Register tidak berhasil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isParamValid(){
        if (mName.getText().toString().trim().isEmpty()){
            mName.setError(getString(R.string.error_empty_name));
            return false;
        }

        if (mEmail.getText().toString().trim().isEmpty()){
            mEmail.setError(getString(R.string.error_empty_email));
            return false;
        }

        if (mPassword.getText().toString().isEmpty()){
            mPassword.setError(getString(R.string.error_empty_password));
            return false;
        }

        if (mPassword.getText().toString().length() <= 4){
            mPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
