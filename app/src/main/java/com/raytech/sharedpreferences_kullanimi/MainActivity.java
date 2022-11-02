package com.raytech.sharedpreferences_kullanimi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextUserPassword;
    private String userName, userPassword, getUserName, getUserPassword;
    private CheckBox checkBox;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean check;
    private Button buttonChangeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         LoadLocale();
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.edtTxtUserName);
        editTextUserPassword = (EditText) findViewById(R.id.edtTxtUserPassword);
        checkBox = (CheckBox) findViewById(R.id.checkRemember);

        //dil desteği
        buttonChangeLanguage = (Button) findViewById(R.id.btnChangeLanguage);
        buttonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ekrana verilecek olan dil seçenekleri metotu
                showChangeLanguageDialog();
            }
        });


        preferences = this.getSharedPreferences("com.raytech.sharedpreferences_kullanimi", Context.MODE_PRIVATE);

        getUserName = preferences.getString("username", null);
        getUserPassword = preferences.getString("password", null);
        check = preferences.getBoolean("checkbox", false);

        if (check && !TextUtils.isEmpty(getUserName) && !TextUtils.isEmpty(getUserPassword)) {
            editTextUsername.setText(getUserName);
            editTextUserPassword.setText(getUserPassword);
            checkBox.setChecked(check);
        }
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"Arabic", "Turkish", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("ar");
                    recreate();
                } else if (i == 1) {
                    setLocale("tr");
                    recreate();
                }
                if (i == 2) {
                    setLocale("en");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
       SharedPreferences.Editor editor  = getSharedPreferences("settings",MODE_PRIVATE).edit();
       editor.putString("My Lang",lang);
       editor.apply();
    }
    public  void LoadLocale(){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My Lang","");
        setLocale(language);
    }
    public void btnLogin(View v) {
        userName = editTextUsername.getText().toString();
        userPassword = editTextUserPassword.getText().toString();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassword)) {
            if (checkBox.isChecked()) {
                editor = preferences.edit();
                editor.putString("username", userName);
                editor.putString("password", userPassword);
                editor.putBoolean("checkbox", true);
                editor.apply();
                //kullanıcı adı ve şifre kayıt başarılı
                Toast.makeText(getApplicationContext(), R.string.user_and_password_successful, Toast.LENGTH_SHORT).show();
            } else {
                editor = preferences.edit();
                editor.putString("username", null);
                editor.putString("password", null);
                editor.putBoolean("checkbox", false);
                editor.apply();

                //giriş başarılı
                Toast.makeText(getApplicationContext(), R.string.login_successful, Toast.LENGTH_SHORT).show();

            }

        } else
            //kullanıcı adı veya şifre boş olamaz
            Toast.makeText(getApplicationContext(), R.string.user_and_password_empty, Toast.LENGTH_SHORT).show();
    }
}