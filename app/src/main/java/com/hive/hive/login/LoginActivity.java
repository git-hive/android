package com.hive.hive.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hive.hive.R;

public class LoginActivity extends AppCompatActivity {

    //Logging TAG
    private static final String TAG = LoginActivity.class.getSimpleName();

    //Sign-in request code
    private static final int RC_SIGN_IN = 9001;

    //Google Sign-in client
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    //Firebase Authenticator
    private FirebaseAuth mAuth;

    //Layout Elements
    private SignInButton mGoogleSignInBT;
    private LoginButton mFacebookSignInBtn;
    private TextView mSignUpTV;
    private TextView mUserTV;
    private TextView mPwdTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // firebase authenticator
        mAuth = FirebaseAuth.getInstance();

        // on android Jellybean or lower, use this call to hide the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // REMEMBER: you should never show the action bar if status bar is hidden,
            // so hide it if necessary.
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();
        }

        // get email and pwd fields
        mUserTV = findViewById(R.id.mUser);
        mUserTV = findViewById(R.id.mPassword);

        // get common sign up button
        mSignUpTV = findViewById(R.id.mSignUp);

        // register click listener on common sign up button
        mSignUpTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUserTV.getText().toString().trim();
                String pwd = mUserTV.getText().toString().trim();
                // TODO: validate email and pwd
                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkUserAndStartSignUpActivity();
                                } else {
                                    Log.w(TAG, "auth with email failed");
                                }
                            }
                        });
            }
        });

        // google sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // get google sign-in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // get google sign in button
        mGoogleSignInBT = findViewById(R.id.text_google_sign_in_button);

        // sets sign in listener
        mGoogleSignInBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // start google sign in intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // get facebook login button
        mFacebookSignInBtn = findViewById(R.id.text_facebook_sign_in_button);

        // set facebook login request permissions
        mFacebookSignInBtn.setReadPermissions("email", "public_profile");

        // create facebook login callback manager
        mCallbackManager = CallbackManager.Factory.create();

        // register fb login button callback
        mFacebookSignInBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

                checkUserAndStartSignUpActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook login failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserAndStartSignUpActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if returned from google sign in
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "google sign in failed", e);
            }
        }

        // facebook sign in callback manager
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Authenticates in Firebase with Google Account
     *
     * @param account - google account to authenticate
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential: success");
                            checkUserAndStartSignUpActivity();
                        } else {
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_main), R.string.auth_failure, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Authenticates in Firebase with Facebook Account
     *
     * @param token - token received from facebook callback
     */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            checkUserAndStartSignUpActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    private void checkUserAndStartSignUpActivity() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Context context = LoginActivity.this.getApplicationContext();
            Intent tartSignUpActivity = new Intent(context, SignupActivity.class);

            tartSignUpActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tartSignUpActivity);

            finish();
        }
    }
}