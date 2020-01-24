package com.madd.madd.intentpractice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    List<Contact> contactList;
    ContactAdapterEvents contactAdapterEvents;

    public ContactAdapter(List<Contact> contactList, ContactAdapterEvents contactAdapterEvents) {
        this.contactList = contactList;
        this.contactAdapterEvents = contactAdapterEvents;
    }



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_contact,viewGroup,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder viewHolder, int i) {
        viewHolder.setData(contactList.get(i));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewInitial;
        private TextView textViewName;
        private TextView textViewNumber;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewInitial = itemView.findViewById(R.id.TV_Contact_Initial);
            textViewName = itemView.findViewById(R.id.TV_Contact_Name);
            textViewNumber = itemView.findViewById(R.id.TV_Contact_Number);
        }

        void setData(Contact contact){
            textViewInitial.setText(String.valueOf(contact.name.toUpperCase().charAt(0)));
            textViewName.setText(contact.name);
            textViewNumber.setText(contact.number);

            itemView.setOnClickListener(view -> {
                contactAdapterEvents.onContactClick(contact);
            });

        }
    }




    interface ContactAdapterEvents{
        void onContactClick(Contact contact);
    }

}
