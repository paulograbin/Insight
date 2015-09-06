package com.paulograbin.insight.LocationEngine;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Exceptions.NoWayException;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;

import junit.framework.Assert;

/**
 * Created by paulograbin on 20/08/15.
 */
public class TestRouteFinder extends ApplicationTestCase<Application> {

    private PlaceProvider mPlaceProvider;
    private PathProvider mPathProvider;
    private RouteFinder mRouteFinder;

    private Place mInitialPlace;
    private Place mFinalPlace;
    private Place mNowherePlace;


    public TestRouteFinder() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        cleanTables();

        standardPlacesAndPaths();

        instantiatePlaces();
    }

    public void testExistingPlace() {
        mRouteFinder = new RouteFinder(getContext(), mInitialPlace, mFinalPlace);
        assertNotNull(mRouteFinder);

        try {
            mRouteFinder.getPathToTargetPlace();
        } catch(NoWayException e) {
            Assert.fail();
        }
    }

    public void testNonExistingPlace() {
        mRouteFinder = new RouteFinder(getContext(), mFinalPlace, mNowherePlace);
        assertNotNull(mRouteFinder);

        try {
            mRouteFinder.getPathToTargetPlace();
            Assert.fail();
        } catch(NoWayException e) {

        }
    }

    private void instantiatePlaces() {
        mInitialPlace = mPlaceProvider.getByName("Ponto Inicial");
        assertNotNull(mInitialPlace);
        assertEquals("Ponto Inicial", mInitialPlace.getName());

        mFinalPlace = mPlaceProvider.getByName("Ponto Final");
        assertNotNull(mFinalPlace);
        assertEquals("Ponto Final", mFinalPlace.getName());

        mNowherePlace = mPlaceProvider.getByName("Nowhere");
        assertNotNull(mNowherePlace);
        assertEquals("Nowhere", mNowherePlace.getName());
    }

    private void cleanTables() {
        mPlaceProvider = new PlaceProvider(getContext());
        mPlaceProvider.deleteAll();
        assertEquals(0, mPlaceProvider.getCount());

        mPathProvider = new PathProvider(getContext());
        mPathProvider.deleteAll();
        assertEquals(0, mPathProvider.getCount());
    }

    private void standardPlacesAndPaths() {
        /*
         * Places
         */
        Place pInitial = new Place("Ponto Inicial", "Um ponto no inicio mapa", "Mensagem de teste!", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.78440, -51.14400);
        Place pMid = new Place("Caminho entre pontos", "Um caminho no meio do mapa", "", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.91305, -51.18932);
        Place pNowhere = new Place("Nowhere", "Algum lugar perdido", "", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.99447, -50.78694);
        Place pEnd = new Place("Ponto Final", "Um ponto no fim do mapa", "", Place.FAVORITE, Place.FINAL_DESTINATION, -30.03201, -51.21678);

        PlaceProvider pp = new PlaceProvider(getContext());
        pInitial.setId(pp.insert(pInitial));
        pMid.setId(pp.insert(pMid));
        pEnd.setId(pp.insert(pEnd));
        pNowhere.setId(pp.insert(pNowhere));

        /*
         * Path
         */
        Path ph1 = new Path(pInitial.getId(), pMid.getId(), 1);     // 10   20
        Path ph3 = new Path(pInitial.getId(), pNowhere.getId(), 1); // 10   30
        Path ph2 = new Path(pMid.getId(), pEnd.getId(), 1);         // 20   40
        Path ph4 = new Path(pMid.getId(), pNowhere.getId(), 1);

        PathProvider ph = new PathProvider(getContext());
        ph1.setId(ph.insert(ph1));
        ph2.setId(ph.insert(ph2));
        ph3.setId(ph.insert(ph3));
        ph4.setId(ph.insert(ph4));
    }
}
