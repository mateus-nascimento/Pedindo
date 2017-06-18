package ledare.com.br.pedindo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.util.Constants;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    //UI
    private TextInputEditText mEmailView, mPasswordView;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI
        mEmailView = (TextInputEditText) findViewById(R.id.header_email);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_register).setOnClickListener(this);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                evaluateFields();
                break;
            case R.id.text_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void evaluateFields() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgressDialog();
            loginWithEmail(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            String message;
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                message = getString(R.string.error_account_wrong);
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = getString(R.string.error_password_wrong);
                            } else if (task.getException() instanceof FirebaseNetworkException) {
                                message = getString(R.string.error_network);
                            } else {
                                message = task.getException().getMessage();
                            }

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                        } else {
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            if (!firebaseUser.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this,
                                        getString(R.string.error_verify_account),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                mDatabase.child(firebaseUser.getUid())
                                        .child(Constants.DATABASE_USER_ACTIVE)
                                        .setValue(true);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }
}
