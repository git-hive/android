package com.hive.hive.login;

import android.content.Context; import android.content.Intent; import android.os.Build;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.hive.hive.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting Layout Elements References From Login Activity
        mGoogleSignInBT = findViewById(R.id.text_google_sign_in_button);
        mFacebookSignInBtn = findViewById(R.id.text_facebook_sign_in_button);
        mSignUpTV = findViewById(R.id.mSignUp);

        // Set facebook login request permissions
        mFacebookSignInBtn.setReadPermissions("email", "public_profile");

        //Sign-in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Sign-in Client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();

        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the'
            // status bar is hidden, so hide that too if necessary.
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.hide();
        }

        //Sets SignIn Listener
        mGoogleSignInBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

                Context context = LoginActivity.this.getApplicationContext();
                Intent startSignUpActivity = new Intent(context, SignupActivity.class);
                
                startSignUpActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startSignUpActivity);

                finish();
            }
        });

        //Sets SignUp Listener
        mSignUpTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LoginActivity.this.getApplicationContext();
                Class destinationActivity = SignupActivity.class;
                Intent startNewActivityIntent = new Intent(context, destinationActivity);
                //startNewActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startNewActivityIntent);
                //finish(); // Call once you redirect to another activity
            }
        });

        // Register facebook login callback
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookSignInBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

                Context context = LoginActivity.this.getApplicationContext();
                Intent startMainActivityIntent = new Intent(context, MainActivity.class);

                startMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMainActivityIntent);

                finish();
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Facebook login failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Signs User By Google Account
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {    //Return from SignIn
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    /**
     * Authenticates in Firebase with Google Account
     * @param account - google account to authenticate
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: "+ account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential: failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_main), R.string.auth_failure, Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    /**
     * Authenticates in Firebase with Facbebook Account
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
                            FirebaseUser user = mAuth.getCurrentUser();
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
}
