package com.example.kappapridesms;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.junit.Assert.*;

/**
 *  Deals with the testing of certain permissions needed to run the KappaPride SMS
 *  The permissions tested include:
 *      -Receiving SMS messages
 *      -Sending SMS messages
 *      -Reading SMS messages
 *      -Reading the Phone information (ex: phone number)
 *      -Reading Contacts from Contact list
 *      -Writing to the Contact list
 */
@RunWith(JUnit4.class)
public class MessageActivityTest
{
    Context m_appContext;

    /**
     *  Test fixture
     */
    @Before
    public void setUp()
    {
        m_appContext = KappaApplication.getAppContext();
    }

/**
 *  This method groups the permissions that deal with SMS messages, and tests whether the permission was granted or  for the following features:
 *      -Receiving SMS messages
 *      -Sending SMS messages
 *      -Reading SMS messages
 */
    @Test
    public void testPermissions1()
    {
        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.RECEIVE_SMS), PackageManager.PERMISSION_GRANTED);
        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.SEND_SMS), PackageManager.PERMISSION_GRANTED);
        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.READ_SMS), PackageManager.PERMISSION_GRANTED);

    }

/**
*  This method tests the permissions allowed for viewing personal phone information.
*       -(ex: phone number)
*/
    @Test
    public void testPermissions2() {

        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE), PackageManager.PERMISSION_GRANTED);
    }

/**
*  This method groups the permissions that deal with the Contact List, and tests whether the permission was granted or not for the following features:
*       -Reading Contacts from Contact list
*       -Writing to the Contact list
*/
    @Test
    public void testPermissions3() {

        // TESTING READ CONTACTS AND WRITE CONTACTS
        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.READ_CONTACTS), PackageManager.PERMISSION_GRANTED);
        assertEquals(m_appContext.checkSelfPermission(Manifest.permission.WRITE_CONTACTS), PackageManager.PERMISSION_GRANTED);

    }




}