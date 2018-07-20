package com.example.quentin.fgcapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by stemlab on 11/20/2017.
 */


public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private Button loginButton, registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //findViewById(R.id.sign_in_button).setOnClickListener((View.OnClickListener) this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

    }

/*

    private void initUI()
    {
        registerButton = (Button) findViewById( R.id.registerButton );
        emailEdit = (EditText) findViewById( R.id.emailEdit );
        passwordEdit = (EditText) findViewById( R.id.passwordEdit);
        loginButton = (Button) findViewById( R.id.loginButton );
        skipView = (TextView) findViewById(R.id.skip_view);



        loginButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                onLoginButtonClicked();
            }
        } );

        registerButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                onRegisterButtonClicked();
            }
        } );

        skipView.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                onSkipViewClicked();
            }
        } );

    }

    public void onLoginButtonClicked()
    {
        String identity = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();


        Backendless.UserService.login( identity, password, new DefaultCallback<BackendlessUser>( LoginActivity1.this )
        {
            public void handleResponse( BackendlessUser backendlessUser )
            {
                super.handleResponse( backendlessUser );
                startActivity( new Intent( LoginActivity1.this, MapsActivity.class ) );
                finish();
            }
        } );
    }

    public void onRegisterButtonClicked()
    {
        startActivity( new Intent( this, SignUpActivity.class ) );
        finish();
    }
    */

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_view:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }
    /* private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    } */
}
