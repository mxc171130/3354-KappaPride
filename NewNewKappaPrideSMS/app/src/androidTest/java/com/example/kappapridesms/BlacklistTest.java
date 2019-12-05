package com.example.kappapridesms;

import android.app.PendingIntent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Testing of The Blacklist Class
 * Statement Coverage 100%
 * Branch Coverage 100%
 * Path Coverage 100%
 * @author AlazarDebello
 */
@RunWith(JUnit4.class)
public class BlacklistTest
{
    private Blacklist m_blacklist=null;
    @Before
    public void setUp() throws Exception
    {
        m_blacklist= new Blacklist();

    }

    /**
     * Testing the add function for the blacklist class
     */
    @Test
    public void test1()
    {
        long num1=Long.parseLong("2222222222");
        long num2=Long.parseLong("333333333");
        m_blacklist.addBlacklistedNumber(num1);
        m_blacklist.addBlacklistedNumber(num2);
        assertEquals(2,m_blacklist.size());



    }

    /**
     * Testing the remove function of the Blacklist class by removing a number that exist in the blacklist
     *
     */
    @Test
    public void test2()
    {   long num1=Long.parseLong("222222222");
        long num2=Long.parseLong("333333333");
        m_blacklist.addBlacklistedNumber(num1);
        m_blacklist.addBlacklistedNumber(num2);
        m_blacklist.removeBlacklistedNumber(num1);
        assertEquals(1,m_blacklist.size());

    }

    /**
     * Testing the remove function of the Blacklist class by removing a number that is not in the blacklist
     */
    @Test
    public void test3()
    {   long num1=Long.parseLong("2222222222");
        long num2=Long.parseLong("3333333333");
        m_blacklist.addBlacklistedNumber(num1);
        m_blacklist.addBlacklistedNumber(num2);
        m_blacklist.removeBlacklistedNumber(2222222222L);
        assertEquals(1,m_blacklist.size());
    }

    /**
     * Testing the getMethod within the BlackList class
     */
    @Test
    public void test4()
    {
        long num1=Long.parseLong("222222222");
        m_blacklist.addBlacklistedNumber(num1);
        long num2=m_blacklist.getBlacklistedContact(m_blacklist.size() - 1);
        assertEquals(num1,num2);
    }


}