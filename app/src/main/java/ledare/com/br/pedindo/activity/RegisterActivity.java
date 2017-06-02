package ledare.com.br.pedindo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ledare.com.br.pedindo.R;
import ledare.com.br.pedindo.model.User;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    //Username
    private String inputName;

    //UI
    private TextInputEditText mNameView, mEmailView, mPasswordView;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //UI
        mNameView = (TextInputEditText) findViewById(R.id.header_name);
        mEmailView = (TextInputEditText) findViewById(R.id.header_email);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        findViewById(R.id.button_register).setOnClickListener(this);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.node_users));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                evaluateFields();
                break;
        }
    }

    private void evaluateFields() {

        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        inputName = mNameView.getText().toString();
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

        if (TextUtils.isEmpty(inputName)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isNameValid(inputName)) {
            mNameView.setError(getString(R.string.error_invalid_username));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgressDialog();
            registerWithEmail(email, password);
        }
    }

    private boolean isNameValid(String name) {
        return !TextUtils.isEmpty(name) && name.trim().length() > 3;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() > 5;
    }

    private void registerWithEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                toastLong(getString(R.string.error_invalid_password));
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                toastLong(getString(R.string.error_invalid_email));
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                toastLong(getString(R.string.error_account_used));
                            } else if (task.getException() instanceof FirebaseNetworkException) {
                                toastShort(getString(R.string.error_network));
                            } else {
                                toastLong(task.getException().getMessage());
                            }
                        } else{
                            final FirebaseUser firebaseUser = task.getResult().getUser();
                            sendVerification(firebaseUser);
                            insertUser(firebaseUser);
                            finish();
                        }
                    }
                });
    }

    private void sendVerification(final FirebaseUser firebaseUser) {
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            toastLong(getString(R.string.verification_send) + firebaseUser.getEmail());
                        } else {
                            toastShort(getString(R.string.verification_error));
                        }
                    }
                });
    }

    private void insertUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.id = firebaseUser.getUid();
        user.username = inputName;
        user.email = firebaseUser.getEmail();
        user.active = false;

        mDatabase.child(firebaseUser.getUid()).setValue(user);
    }
}
