package com.mhgroup.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.mhgroup.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DK Wang on 2015/3/20.
 */
public class ContactsGetter {

    private Context context;

    public ContactsGetter(){}

    public ContactsGetter(Context context)
    {
        this.context = context;
    }

    public List<Contact> getContacts()
    {
        List<Contact> contacts = new ArrayList<Contact>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if(cur.getCount() > 0)
        {
            while(cur.moveToNext())
            {
                Contact contact = new Contact();

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                contact.setName(name);

                if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"" +
                                    " = ?",
                            new String[]{id}, null
                    );
                    while(pCur.moveToNext())
                    {
                        String phoneNum = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.addPhoneNumber(phoneNum);
                    }
                    pCur.close();
                }

                contacts.add(contact);

            }
        }
        return contacts;
    }


}
