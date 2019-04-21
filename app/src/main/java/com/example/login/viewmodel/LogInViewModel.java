package com.example.login.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import com.android.databinding.library.baseAdapters.BR;
import com.example.login.Model.User;
import com.example.login.View.SecondActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogInViewModel extends BaseObservable
{
    User user;
    FirebaseAuth mAuth;
    Context ctx;

    String successMessage = "success";
    String errorMessage = "failure";
    String cannotSignIn = "Cannot sign in";

    @Bindable
    String toastMessage = null;

    public LogInViewModel(FirebaseAuth auth,Context context)
    {
         user = new User("","");
         mAuth = auth;
         ctx = context;
    }


    @Bindable
    public String getUserEmail()
    {
        return user.getEmail();
    }

    @Bindable
    public String getUserPassword()
    {
        return user.getPassword();
    }

    public void setUserEmail(String userEmail)
    {
        user.setEmail(userEmail);
        notifyPropertyChanged(BR.userEmail);
    }

    public void setUserPassword(String userPassword)
    {
        user.setPassword(userPassword);
        notifyPropertyChanged(BR.userPassword);
    }


    public void onLoginClicked()
    {
        if(!isInputValid())
        {
            setToastMessage(errorMessage);
        }
        else
        {
            setToastMessage(successMessage);

            mAuth.createUserWithEmailAndPassword(getUserEmail(),getUserPassword())
                    .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Intent i = new Intent(ctx, SecondActivity.class);
                                ctx.startActivity(i);
                            }
                            else
                            {
                                setToastMessage(cannotSignIn);
                            }
                        }
                    });
        }



    }

    private void setToastMessage(String message)
    {
        toastMessage = message;
        notifyPropertyChanged(BR.toastMessage);
    }


    public String getToastMessage()
    {
        return toastMessage;
    }

    public boolean isInputValid()
    {
        return !TextUtils.isEmpty(getUserEmail())
                && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches()
                && getUserPassword().length() > 5;
    }
}