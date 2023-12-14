package com.example.multimediaapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter2 extends RecyclerView.Adapter<ContactAdapter2.ContactViewHolder>{
    private List<ContactItem2> contacts;
    private static final int REQUEST_CODE_CALL_PHONE = 102;
    public ContactAdapter2(List<ContactItem2> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactAdapter2.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contact_item2, parent, false);
        return new ContactAdapter2.ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter2.ContactViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        ContactItem2 contact = contacts.get(position);
        holder.textViewIsim.setText(contact.getIsim());
        holder.textViewTelefon.setText(contact.getTelefonNumarasi());


        holder.imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);

                    String phoneNumber = contact.getTelefonNumarasi();


                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));


                    v.getContext().startActivity(callIntent);
                } else {

                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
    public void setContacts(List<ContactItem2> newContacts) {
        contacts.clear();
        contacts.addAll(newContacts);
        notifyDataSetChanged();
    }
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIsim;
        TextView textViewTelefon;
        ImageView imageViewPhone; // ImageView tanımı burada

        ContactViewHolder(View itemView) {
            super(itemView);
            textViewIsim = itemView.findViewById(R.id.textViewIsim);
            textViewTelefon = itemView.findViewById(R.id.textViewTelefon);
            imageViewPhone = itemView.findViewById(R.id.imageViewPhone); // ImageView'a erişim burada
        }
    }
}
