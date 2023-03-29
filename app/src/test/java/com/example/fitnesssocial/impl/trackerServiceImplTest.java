package com.example.fitnesssocial.impl;

import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class trackerServiceImplTest{
    private trackerServiceImpl impl;

    @Before
    public void setUp(){
        this.impl = new trackerServiceImpl();
    }

    @Test
    public void test1(){
        this.impl.func1();
        assertEquals(1, 1);
    }

    @Test
    public void test2(){
        this.impl.func2();
        assertEquals(1, 1);
    }

    @Test
    public void test3(){
        this.impl.func3();
        assertEquals(1, 1);
    }
}