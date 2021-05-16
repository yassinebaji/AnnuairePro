package com.example.annuaire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    AppDatabase database;

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    EditText job;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database=AppDatabase.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        firstName = (EditText) findViewById(R.id.fn_filed);
        lastName = (EditText) findViewById(R.id.ln_filed);
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.num_filed);
        job = (EditText) findViewById(R.id.job_filed);

        Intent i = getIntent();
        s = i.getStringExtra("ID");

        Contact contact = database.contact().findByID(Integer.parseInt(s));

        firstName.setText(contact.getName().split(" ",0)[0]);
        if(contact.getName().split(" ",0).length==2)
        lastName.setText(contact.getName().split(" ",0)[1]);
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());
        job.setText(contact.getJob());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_update,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id==R.id.update){
            database.contact().update(new Contact(Integer.parseInt(s),firstName.getText().toString() + " " + lastName.getText().toString(),email.getText().toString(),job.getText().toString(),phone.getText().toString()));
            Toast.makeText(EditActivity.this,"Updated",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}