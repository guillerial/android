package es.indios.markn.ui.login;

import android.content.Intent;
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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_button:
                String email = mEmailEditText.getText().toString();
                String password = mPassEditText.getText().toString();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && !password.equals("")){
                    mLoginPresenter.logIn(email, password);
                }
                break;
            default:
                break;
        }
    }
}
