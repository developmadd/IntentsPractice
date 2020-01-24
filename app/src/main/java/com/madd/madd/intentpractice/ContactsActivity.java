package com.madd.madd.intentpractice;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity {

    @BindView(R.id.RV_Contacts) RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ButterKnife.bind(this);

        contactAdapter = new ContactAdapter(contactList, contact -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("phone",contact.number);
            setResult(RESULT_OK,resultIntent);
            finish();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactAdapter);

        getDeviceContacts();

    }


    private void getDeviceContacts(){


        Cursor contacts = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);
        while (contacts.moveToNext()){

            boolean hasPhoneNumber = Integer.parseInt(
                    contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) != 0;
            if( hasPhoneNumber  ) {

                String name = contacts.getString(
                        contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String id = contacts.getString(
                        contacts.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = " + id,
                        null,
                        null);

                while (phones.moveToNext()){
                    String number = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    number = number.replace("(" ,"");
                    number = number.replace(")" ,"");
                    number = number.replace("+" ,"");
                    number = number.replaceAll(" " ,"");
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    contactList.add( new Contact(name,number));
                    break;
                }
            }

        }

        contactAdapter.notifyDataSetChanged();

    }



}
