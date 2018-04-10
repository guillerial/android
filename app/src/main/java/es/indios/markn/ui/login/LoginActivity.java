package es.indios.markn.ui.login;

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
import es.indios.markn.ui.signup.SignUpActivity;
import es.indios.ribot.androidboilerplate.R;

public class LoginActivity extends BaseActivity implements LoginMvpView, View.OnClickListener{

    @BindView(R.id.email_edittext)              EditText mEmailEditText;
    @BindView(R.id.password_edittext)           EditText mPassEditText;
    @BindView(R.id.forgot_password_textview)    TextView mForgotPassTextView;
    @BindView(R.id.login_button)                Button mLoginButton;
    @BindView(R.id.textview_create_account)     TextView mCreateAccountTextView;
    @BindView(R.id.textview_guest_session)      TextView mGuestSession;

    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        mLoginPresenter.attachView(this);

        mLoginPresenter.checkLoggedIn();

        mLoginButton.setOnClickListener(this);
        mForgotPassTextView.setOnClickListener(this);
        mGuestSession.setOnClickListener(this);
        mCreateAccountTextView.setOnClickListener(this);
    }



    @Override
    protected void onDestroy() {
        mLoginPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onLoggedUser() {
        Intent intent = new Intent(this, InitActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onForbiddenLogin(int code) {
        mEmailEditText.setHint(getResources().getString(R.string.text_user_no_exist));
        mEmailEditText.setHintTextColor(Color.RED);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_button:
                String email = mEmailEditText.getText().toString();
                String password = mPassEditText.getText().toString();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.equals("")){
                    mLoginPresenter.logIn(email, password);
                }else{
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        mEmailEditText.setHint(getResources().getString(R.string.text_valid_email));
                        mEmailEditText.setHintTextColor(Color.RED);
                    }
                    if(password.equals("")){
                        mPassEditText.setHint(getResources().getString(R.string.text_pass_required));
                        mPassEditText.setHintTextColor(Color.RED);
                    }
                }
                break;
            case R.id.forgot_password_textview:

                break;
            case R.id.textview_guest_session:
                onLoggedUser();
                break;
            case R.id.textview_create_account:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
