package es.indios.markn.ui.signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.indios.markn.ui.base.BaseActivity;
import es.indios.markn.ui.init.InitActivity;
import es.indios.ribot.androidboilerplate.R;

public class SignUpActivity extends BaseActivity implements SignUpMvpView, View.OnClickListener {


    @Inject
    SignUpPresenter mSignUpPresenter;

    @BindView(R.id.name_edittext) EditText mUsernameEditText;
    @BindView(R.id.signup_email_edittext) EditText mEmailEditText;
    @BindView(R.id.signup_password_edittext) EditText mPassEditText;
    @BindView(R.id.signup_textview_guest_session) TextView mGuestSessionTextView;
    @BindView(R.id.signup_button) Button mSignUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_signup);
        mSignUpPresenter.attachView(this);
        ButterKnife.bind(this);

        mSignUpButton.setOnClickListener(this);
        mGuestSessionTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_button:
                String name = mUsernameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPassEditText.getText().toString();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.equals("")&& !name.equals("")){
                    mSignUpPresenter.signUp(name, email, password);
                }else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        //TODO: poner el hint en Strings
                        //mEmailEditText.setHint();
                        mEmailEditText.setHintTextColor(Color.RED);
                    }
                    if(password.equals("")){
                        //TODO: poner el hint en Strings
                        //mPassEditText.setHint();
                        mPassEditText.setHintTextColor(Color.RED);
                    }
                    if(name.equals("")){
                        //TODO: poner el hint en Strings
                        //mUsernameEditText.setHint();
                        mUsernameEditText.setHintTextColor(Color.RED);
                    }
                }
                break;
            case R.id.signup_textview_guest_session:
                onLoggedUser();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoggedUser() {
        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onForbiddenLogin(int code) {
        //TODO: poner el hint en Strings
        //mEmailEditText.setHint();
        mEmailEditText.setHintTextColor(Color.RED);
    }
}
