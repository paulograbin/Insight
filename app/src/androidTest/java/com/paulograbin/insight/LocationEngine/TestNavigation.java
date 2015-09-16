package com.paulograbin.insight.LocationEngine;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.DatabaseHelper;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;

import junit.framework.Assert;

/**
 * Created by paulograbin on 20/08/15.
 */
public class TestNavigation extends ApplicationTestCase<Application> {

    Navigation mNavigation;
    RouteFinder mRouteFinder;
    PlaceProvider mPlaceProvider;

    Place mCurrentPlace;

    Place mPreviousPlace;
    Place mNextPlace;

    Place mSourcePlace;
    Place mTargetPlace;


    public TestNavigation() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DatabaseHelper.getInstance(getContext()).checkDatabase();

        mPlaceProvider = new PlaceProvider(getContext());

        mSourcePlace = mPlaceProvider.getByName("Ponto Inicial");
        assertNotNull(mSourcePlace);
        mTargetPlace = mPlaceProvider.getByName("Ponto Final");
        assertNotNull(mTargetPlace);

        mRouteFinder = new RouteFinder(getContext(), mSourcePlace, mTargetPlace);
    }

    public void testPathDistance() {
        mNavigation = new Navigation(mRouteFinder.getPath(), mSourcePlace, mTargetPlace);

        assertNotSame(0, mNavigation.getRemainingDistance());
    }

    public void testNavigation() throws Exception {
        mNavigation = new Navigation(mRouteFinder.getPath(), mSourcePlace, mTargetPlace);
        assertNotNull(mNavigation);
        testPathSize(3);

        assertEquals(-1, mNavigation.getCurrentPlaceNumber());
        assertFalse(mNavigation.isLastPlace());

        // PREVIOUS AND NEXT
        testPreviousPlace(null);
        testNextPlace("Ponto Inicial");

        // FIRST PLACE
        mCurrentPlace = mNavigation.getNextPlace();
        assertEquals("Ponto Inicial", mCurrentPlace.getName());
        assertFalse(mNavigation.isLastPlace());
        assertEquals("Ponto Inicial", mNavigation.getCurrentPlace().getName());
        // PREVIOUS AND NEXT
        testPreviousPlace(null);
        testNextPlace("Caminho entre pontos");


        // SECOND PLACE
        mCurrentPlace = mNavigation.getNextPlace();
        assertEquals("Caminho entre pontos", mCurrentPlace.getName());
        assertFalse(mNavigation.isLastPlace());
        assertEquals("Caminho entre pontos", mNavigation.getCurrentPlace().getName());
        // PREVIOUS AND NEXT
        testPreviousPlace("Ponto Inicial");
        testNextPlace("Ponto Final");

        // LAST PLACE
        mCurrentPlace = mNavigation.getNextPlace();
        assertEquals("Ponto Final", mCurrentPlace.getName());
        assertTrue(mNavigation.isLastPlace());
        assertEquals("Ponto Final", mNavigation.getCurrentPlace().getName());
        // PREVIOUS AND NEXT
        testPreviousPlace("Caminho entre pontos");
        testNextPlace(null);


        // PAST THE LAST PLACE
        try {
            mCurrentPlace = mNavigation.getNextPlace();
            Assert.fail("Should have thrown an exception");
        } catch (RuntimeException e) {
        }
    }

    private void testPreviousPlace(String expetectedName) {
        if (expetectedName == null) {
            testPreviousPlaceNull();
        } else {
            testPreviousPlaceWithName(expetectedName);
        }
    }

    public void testPreviousPlaceWithName(String expetectedName) {
        mPreviousPlace = null;

        mPreviousPlace = mNavigation.checkPreviousPlace();

        assertEquals(expetectedName, mPreviousPlace.getName());
    }

    public void testPreviousPlaceNull() {
        mPreviousPlace = null;

        try {
            mPreviousPlace = mNavigation.checkPreviousPlace();
            Assert.fail("Shoud've thrown an exception");
        } catch (RuntimeException e) {
        }

        assertNull(mPreviousPlace);
    }

    private void testNextPlace(String expectedName) {
        if (expectedName == null) {
            testNextPlaceNull();
        } else {
            testNextPlaceWithName(expectedName);
        }

    }

    public void testNextPlaceWithName(String expetectedName) {
        mNextPlace = null;

        mNextPlace = mNavigation.checkNextPlace();

        assertEquals(expetectedName, mNextPlace.getName());
    }

    public void testNextPlaceNull() {
        mNextPlace = null;

        try {
            mNextPlace = mNavigation.checkNextPlace();
            Assert.fail("Shoud've thrown an exception");
        } catch (RuntimeException e) {
        }

        assertNull(mNextPlace);
    }

    public void testPathSize(int size) throws Exception {
        assertEquals(size, mNavigation.getPathSize());
    }
}
