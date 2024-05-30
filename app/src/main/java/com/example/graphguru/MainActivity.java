package com.example.graphguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText nameField ;
    EditText passField ;
    User user = null ;

    private DatabaseHandler databaseHandler;

    public boolean isLogin = false ;

    public boolean CheckLogin()
    {
        String name = nameField.getText().toString() ;
        String pass = passField.getText().toString() ;

        nameField.setText("");
        passField.setText("");
        nameField.clearFocus();
        passField.clearFocus();

        user = databaseHandler.FetchUser(name,pass) ;
        if(user != null)
            return true ;
        return false ;
    }

    public boolean ContainsSpaces(String a)
    {
        for(int i = 0 ; i < a.length() ; i++)
            if(a.charAt(i) == ' ')
                return true ;
        return false ;
    }

    public boolean SignUp()
    {
        String name = nameField.getText().toString() ;
        String pass = passField.getText().toString() ;
        nameField.setText("");
        passField.setText("");

        if(!name.equals("") && !pass.equals("") && !ContainsSpaces(name)) {
            if(databaseHandler.addUser(name,pass) == true) {
                Toast.makeText(MainActivity.this, "ACCOUNT ADDED!", Toast.LENGTH_LONG).show();
                user = databaseHandler.FetchUser(name,pass) ;
                return true ;
            }
            else {
                Toast.makeText(MainActivity.this, "FAILED TO ADD ACCOUNT, INTERNAL ERROR!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else if(!ContainsSpaces(name)) {
            Toast.makeText(MainActivity.this, "FIELDS CANNOT BE EMPTY!", Toast.LENGTH_LONG).show();
            return false ;
        }
        Toast.makeText(MainActivity.this,"THE NAME MUST NOT CONTAIN SPACES!",Toast.LENGTH_LONG ).show();
        return false ;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = new DatabaseHandler(MainActivity.this) ;

        Button loginButton = findViewById(R.id.loginButton) ;
        Button signupButton = findViewById(R.id.registerButton) ;
        nameField = findViewById(R.id.namefield) ;
        passField = findViewById(R.id.passField) ;
        loginButton.setBackgroundColor(Color.rgb(44,44,44));
        signupButton.setBackgroundColor(Color.rgb(185, 248, 152));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButton.setBackgroundColor(Color.rgb(44,44,44));
                loginButton.setBackgroundColor(Color.rgb(185, 248, 152));

                if(CheckLogin())
                {
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class) ;
                    intent.putExtra("name",user.getName()) ;
                    intent.putExtra("graph_collection_table_name",user.getGraph_collection_table_name()) ;
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "ACCOUNT DOES NOT EXIST!", Toast.LENGTH_LONG).show();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SignUp()) {
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.putExtra("name", user.getName());
                    intent.putExtra("graph_collection_table_name", user.getGraph_collection_table_name());
                    startActivity(intent);
                }
                nameField.clearFocus();
                passField.clearFocus();
                loginButton.setBackgroundColor(Color.rgb(44,44,44));
                signupButton.setBackgroundColor(Color.rgb(185, 248, 152));
            }
        });



    }
}