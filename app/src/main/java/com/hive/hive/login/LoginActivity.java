package com.hive.hive.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hive.hive.R;
import com.hive.hive.main.MainActivity;
import com.hive.hive.utils.ProfilePhotoHelper;
import com.hive.hive.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    // Logging TAG
    private static final String TAG = LoginActivity.class.getSimpleName();

    // Sign-in request code
    private static final int RC_SIGN_IN = 9001;

    // Google Sign-in client
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    // Firebase Authenticator
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Firestore database
    private FirebaseFirestore db;

    // Layout Elements
    private SignInButton mGoogleSignInBT;
//    private LoginButton mFacebookSignInBtn;
    private EditText mEditTextPassword;
    private EditText mEditTextUser;
    private TextView mTextViewPassword;
    private TextView mTextViewUser;
    private TextView mTextViewSignUp;
    private TextView mTextViewForgotPass;
    private Button mButtonSignIn;
    private Button mButtonEmailLogin;
    private ProgressBar mPBLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // firebase authenticator
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                checkUserAndSwitchActivity();
            }
        };
        // For debugging purposes
        // mAuth.signOut();
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

        db = FirebaseFirestore.getInstance();

        mPBLogin = findViewById(R.id.progress_bar_login);
        mTextViewPassword = findViewById(R.id.textViewPassword);
        mTextViewUser = findViewById(R.id.textViewUser);
        mTextViewForgotPass = findViewById(R.id.textViewForgotPassword);
        mButtonSignIn = findViewById(R.id.buttonSignIn);
        mButtonEmailLogin = findViewById(R.id.buttonEmailSignIn);
        // get email and pwd fields
        mEditTextUser = findViewById(R.id.editTextUser);
        mEditTextPassword = findViewById(R.id.editTextPassword);

        // get common sign up button
        mTextViewSignUp = findViewById(R.id.mSignUp);
        // register click listener on email method
        mButtonEmailLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTextViewUser.getVisibility() == View.GONE) {
                    mTextViewUser.setVisibility(View.VISIBLE);
                    mTextViewPassword.setVisibility(View.VISIBLE);
                    mTextViewForgotPass.setVisibility(View.VISIBLE);
                    mTextViewSignUp.setVisibility(View.VISIBLE);
                    mEditTextUser.setVisibility(View.VISIBLE);
                    mEditTextPassword.setVisibility(View.VISIBLE);
                    mButtonSignIn.setVisibility(View.VISIBLE);
                    mButtonEmailLogin.setText(R.string.other_login_methods);
//                    mFacebookSignInBtn.setVisibility(View.GONE);
                    mGoogleSignInBT.setVisibility(View.GONE);
                }else{
                    mTextViewUser.setVisibility(View.GONE);
                    mTextViewPassword.setVisibility(View.GONE);
                    mTextViewForgotPass.setVisibility(View.GONE);
                    mTextViewSignUp.setVisibility(View.GONE);
                    mEditTextUser.setVisibility(View.GONE);
                    mEditTextPassword.setVisibility(View.GONE);
                    mButtonSignIn.setVisibility(View.GONE);
                    mButtonEmailLogin.setVisibility(View.VISIBLE);
//                    mFacebookSignInBtn.setVisibility(View.VISIBLE);
                    mGoogleSignInBT.setVisibility(View.VISIBLE);
                    mButtonEmailLogin.setText(R.string.email_login);

                }
            }
        });
        // register click listener on common sign up button
        mTextViewSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Utils.getText(mEditTextUser);
                String pwd = Utils.getText(mEditTextPassword);

                if (TextUtils.isEmpty(email)) {
                    mEditTextUser.setError("e-mail is required");
                    mEditTextUser.requestFocus();
                }
                if (TextUtils.isEmpty(pwd)) {
                    mEditTextPassword.setError("Password is required");
                    mEditTextPassword.requestFocus();
                }

                if((!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd))) {
                    progressUser();
                    hideEmailFields();
                    email = mEditTextUser.getText().toString().trim();
                    pwd = mEditTextPassword.getText().toString().trim();
                  //  System.out.println(email+pwd);
                    // TODO: validate email and pwd
                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                        @Override
                        public void onSuccess(AuthResult authResult) {
                           emailLogin();
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        // Getting failures related to login and password
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(LoginActivity.this, "Authentication failed." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            returnFromProgress();
                        }
                    });
                }
            }
        });

        mButtonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                emailLogin();
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
                progressUser();
            }
        });
        // get facebook login button
//        mFacebookSignInBtn = findViewById(R.id.text_facebook_sign_in_button);

        // set facebook login request permissions
//        mFacebookSignInBtn.setReadPermissions("email", "public_profile");

        // create facebook login callback manager
        mCallbackManager = CallbackManager.Factory.create();

        // register fb login button callback

//        mFacebookSignInBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(LoginActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(LoginActivity.this, "Facebook login failed", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, error.getMessage());
//            }
//        });
        checkUserAndSwitchActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkUserAndSwitchActivity();
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
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
                returnFromProgress();
                Toast.makeText(this, "Failed to sign in with google", Toast.LENGTH_SHORT).show();
            }
        }

        // facebook sign in callback manager
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){

    }
    private void emailLogin(){
        if(mEditTextUser.getText().equals(null)){
            mEditTextUser.requestFocus();
            return;
        }
        if(mEditTextPassword.getText().equals(null)){
            mEditTextPassword.requestFocus();
            return;
        }
        String email = mEditTextUser.getText().toString().trim();
        String pwd = mEditTextPassword.getText().toString().trim();

        // Checking if fields are empty or wrong
        if( mEditTextUser.getText().toString().trim().equals("")){

            mEditTextUser.setError( "Username name is required!" );
            mEditTextUser.setHint("please enter a username");

        }else if(mEditTextPassword.getText().toString().trim().equals("")) {

            mEditTextPassword.setError( "Password is required!" );
            mEditTextPassword.setHint("please enter a password");

        }else{

            mAuth.signInWithEmailAndPassword(email, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkUserAndSwitchActivity();

                }
            }).addOnFailureListener(new OnFailureListener() {
                // Getting failures related to login and password
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        mEditTextUser.requestFocus();
                        mEditTextUser.setError( "A valid User is required!" );
                        mEditTextUser.setHint("please enter a user");
                    }else if(e instanceof FirebaseAuthInvalidCredentialsException){
                        mEditTextPassword.requestFocus();
                        mEditTextPassword.setError( "A valid password is required!" );
                        mEditTextPassword.setHint("please enter a password");
                        Toast.makeText(LoginActivity.this, "Authentication failed." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(LoginActivity.this, "Authentication failed." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
                            checkUserAndSwitchActivity();
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
        mAuth.signInWithCredential(credential);
    }

    private void checkUserAndSwitchActivity() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            //used to show user progress
            progressUser();
            ProfilePhotoHelper.updateProfileUrl();
            // Try to retrieve firestore data
            db
                .collection("users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Switch to SignUpActivity by default
                        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                        if (task.isSuccessful()) {
                            DocumentSnapshot snap = task.getResult();
                            if (snap.exists()) {
                                // If user data exists on Firestore switch to UserProfileActivity
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            }
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        }
    }
    private void hideEmailFields(){
        mTextViewUser.setVisibility(View.GONE);
        mTextViewPassword.setVisibility(View.GONE);
        mTextViewForgotPass.setVisibility(View.GONE);
        mTextViewSignUp.setVisibility(View.GONE);
        mEditTextUser.setVisibility(View.GONE);
        mEditTextPassword.setVisibility(View.GONE);
        mButtonSignIn.setVisibility(View.GONE);;
        mButtonEmailLogin.setText(R.string.email_login);

    }
    //used to show login progress
    private void progressUser(){
        //progressBar
        mPBLogin.setVisibility(View.VISIBLE);
        //buttons
//        mFacebookSignInBtn.setVisibility(View.GONE);
        mGoogleSignInBT.setVisibility(View.GONE);
        mButtonSignIn.setVisibility(View.GONE);
        mButtonEmailLogin.setVisibility(View.GONE);
        //edit texts
        mEditTextUser.setVisibility(View.GONE);
        mEditTextPassword.setVisibility(View.GONE);
        //text views
        mTextViewSignUp.setVisibility(View.GONE);
        mTextViewPassword.setVisibility(View.GONE);
        mTextViewForgotPass.setVisibility(View.GONE);
        mTextViewUser.setVisibility(View.GONE);
    }
    //reshow buttons
    private void returnFromProgress(){
//        mFacebookSignInBtn.setVisibility(View.VISIBLE);
        mGoogleSignInBT.setVisibility(View.VISIBLE);
        mButtonEmailLogin.setVisibility(View.VISIBLE);
        mPBLogin.setVisibility(View.GONE);
    }
}

