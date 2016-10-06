package com.paulograbin.insight.LocationEngine;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.Builders.PlaceBuilder;
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
            mRouteFinder.getPath();
        } catch(NoWayException e) {
            Assert.fail();
        }
    }

    public void testNonExistingPlace() {
        mRouteFinder = new RouteFinder(getContext(), mFinalPlace, mNowherePlace);
        assertNotNull(mRouteFinder);

        try {
            mRouteFinder.getPath();
            Assert.fail();
        } catch(NoWayException ignored) {

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
        Place pInitial = new PlaceBuilder().withName("Ponto Inicial")
                .withDescription("Um ponto no inicio mapa")
                .withMessage("Mensagem de teste!")
                .setDestination()
                .withLatitude(-29.78440)
                .withLongitude(-51.14400)
                .build();

        Place pMid = new PlaceBuilder().withName("Caminho entre pontos")
                .withDescription("Um caminho no meio do mapa")
                .withMessage("")
                .withLatitude(-29.91305)
                .withLongitude(-51.18932)
                .build();

        Place pNowhere = new PlaceBuilder().withName("Nowhere")
                .withDescription("Algum lugar perdido")
                .withMessage("")
                .withLatitude(-29.99447)
                .withLongitude(-50.78694)
                .build();

        Place pEnd = new PlaceBuilder().withName("Ponto Final")
                .withDescription("Um ponto no fim do mapa")
                .withMessage("")
                .setFavorite()
                .setDestination()
                .withLatitude(-30.03201)
                .withLongitude(-51.21678)
                .build();

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
