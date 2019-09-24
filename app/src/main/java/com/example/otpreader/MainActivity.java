package com.example.otpreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String verificationId = "";
    PhoneAuthCredential phoneAuthCredential;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                Log.e("onVerificationCompleted",phoneAuthCredential.toString());
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("onVerificationFailed",e.toString());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(s, forceResendingToken);
                Log.e( "onCodeSent:" , s);
                verificationId = s;

            }
        };

        Log.e("logIs","working");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+917045154988",        // Phone number to verify
                1,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                onVerificationStateChangedCallbacks);

        //phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, "");/////////////////////

    }

    String TAG = "MainActivity";
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("signInWithCredential","success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.e("userIs",user.getDisplayName()+"");
                            Log.e("userIs",user.getEmail()+"");
                            Log.e("userIs",user.getPhoneNumber()+"");
                            Log.e("userIs",user.getUid()+"");
                            Log.e("userIs",user.getPhotoUrl()+"");
                            Log.e("userIs",user.getMetadata()+"");
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    //FirebaseAuth.getInstance().signOut();
}
