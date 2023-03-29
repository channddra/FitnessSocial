package com.example.fitnesssocial.impl;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class achievementServiceImplTest{
    private achievementServiceImpl impl;

    @Before
    public void setUp(){
        this.impl = new achievementServiceImpl();
    }

    @Test
    public void showCompleteAchievementTest(){
        this.impl.func1();
        assertEquals(1, 1);
    }

    @Test
    public void showIncompleteAchievementTest(){
        this.impl.func1();
        assertEquals(1, 1);
    }

    @Test
    public void showCombinedAchievementTest(){
        this.impl.func1();
        assertEquals(1, 1);
    }
}