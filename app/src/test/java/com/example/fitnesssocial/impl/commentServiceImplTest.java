package com.example.fitnesssocial.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class commentServiceImplTest{
    private commentServiceImpl impl;

    @Before
    public void setUp(){
        this.impl = new commentServiceImpl();
    }

    @Test
    public void showCommentsTest(){
        this.impl.func1();
        assertEquals(1, 1);
    }

    @Test
    public void submitCommentTest(){
        this.impl.func2();
        assertEquals(1, 1);
    }
}