package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.Place;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPlaceProvider extends ApplicationTestCase<Application> {

    PlaceProvider mPlaceProvider;

    public TestPlaceProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mPlaceProvider = new PlaceProvider(getContext());
        assertNotNull(mPlaceProvider);

        mPlaceProvider.deleteAll();
        assertEquals(mPlaceProvider.getCount(), 0);
    }

    public void testGetAllFavoritePlacesOnlyOneFavoriteRecord() {
        Place p1 = mPlaceProvider.getDummy();
        p1.setFavorite(1);
        p1.setId(mPlaceProvider.insert(p1));

        assertEquals(1, mPlaceProvider.getAllFavoritePlaces().size());
    }

    public void testGetAllFavoritePlacesNoOneFavoriteRecord() {
        Place p1 = mPlaceProvider.getDummy();
        p1.setId(mPlaceProvider.insert(p1));

        assertEquals(0, mPlaceProvider.getAllFavoritePlaces().size());
    }

    public void testGetAllFavoritePlaces() {
        Place p1 = mPlaceProvider.getDummy();
        p1.setFavorite(1);

        Place p2 = mPlaceProvider.getDummy();
        p2.setFavorite(0);

        p1.setId(mPlaceProvider.insert(p1));
        p2.setId(mPlaceProvider.insert(p2));

        assertEquals(1, mPlaceProvider.getAllFavoritePlaces().size());
    }

    public void testGetByNameExisting() {
        Place p = mPlaceProvider.getDummy();
        p.setName("Ponto Initial");

        long id = mPlaceProvider.insert(p);
        assertNotNull(id);

        try {
            Place b = mPlaceProvider.getByName(p.getName());
            assertEquals(b.getName(), p.getName());
        } catch (RecordNotFoundException e) {

        }
    }

    public void testGetByNameNotExisting() {
        try {
            Place p = mPlaceProvider.getByName("Any name");
            Assert.fail("Should have thrown an SQLiteException");
        } catch (RecordNotFoundException e) {

        }
    }

    public long insertDummyPlaceDestination() {
        Place p = mPlaceProvider.getDummy();
        p.setId(mPlaceProvider.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public long insertDummyPlaceNonDestination() {
        Place p = mPlaceProvider.getDummy();
        p.setDestination(0);

        p.setId(mPlaceProvider.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public void testDeleteByPlaceExisting() {
        long id = insertDummyPlaceDestination();
        assertEquals(mPlaceProvider.getCount(), 1);

        Place m = mPlaceProvider.getByID(id);

        int affectedRows = mPlaceProvider.delete(m);

        assertEquals(affectedRows, 1);
        assertEquals(mPlaceProvider.getCount(), 0);
    }

    public void testDeleteByPlaceNotExisting() {
        Place b = mPlaceProvider.getDummy();
        long id = mPlaceProvider.insert(b);

        int affectedRows = mPlaceProvider.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(mPlaceProvider.getCount(), 0);

        long id = insertDummyPlaceDestination();
        assertEquals(mPlaceProvider.getCount(), 1);

        int affectedRows = mPlaceProvider.delete(id);
        assertEquals(mPlaceProvider.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(mPlaceProvider.getCount(), 0);
        int affetedRows = mPlaceProvider.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoPlace() {
        assertEquals(mPlaceProvider.getCount(), 0);

        List<Place> beacons = mPlaceProvider.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPlace() {
        assertEquals(mPlaceProvider.getCount(), 0);
        List<Place> Places = mPlaceProvider.getAll();

        insertDummyPlaceDestination();
        Places = mPlaceProvider.getAll();
        assertEquals(Places.size(), mPlaceProvider.getCount());
        assertEquals(Places.size(), 1);

        insertDummyPlaceDestination();
        Places = mPlaceProvider.getAll();
        assertEquals(Places.size(), mPlaceProvider.getCount());
        assertEquals(Places.size(), 2);

        insertDummyPlaceDestination();
        Places = mPlaceProvider.getAll();
        assertEquals(Places.size(), mPlaceProvider.getCount());
        assertEquals(Places.size(), 3);

        Place a = Places.get(2);
        Place b = mPlaceProvider.getDummy();

        assertEquals(a.getName(), b.getName());
    }

    public void testGetAllDestination() {
        insertDummyPlaceNonDestination();

        insertDummyPlaceDestination();
        assertEquals(mPlaceProvider.getAllDestinationPlaces().size(), mPlaceProvider.getCount() - 1);
    }

    public void testGetAllNonDestination() {
        insertDummyPlaceDestination();

        insertDummyPlaceNonDestination();
        assertEquals(mPlaceProvider.getAllNonDestinationPlaces().size(), mPlaceProvider.getCount() - 1);
    }

    public void testUpdateExisting() {
        Place a = mPlaceProvider.getDummy();
        long id = mPlaceProvider.insert(a);

        Place b = mPlaceProvider.getByID(id);
        String newText = "123456789";
        b.setName(newText);

        mPlaceProvider.update(b);

        Place c = mPlaceProvider.getByID(id);
        assertEquals(c.getName(), newText);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        Place b = mPlaceProvider.getDummy();

        String newText = "123456789";
        b.setName(newText);

        int affectedRows = mPlaceProvider.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPlace() {
        Place a = mPlaceProvider.getDummy();
        long id = mPlaceProvider.insert(a);

        assertNotSame(id, 0);
        assertEquals(mPlaceProvider.getCount(), 1);

        Place b = mPlaceProvider.getByID(id);
        assertNotNull(b);

        assertEquals(a.getName(), b.getName());
    }

    public void testGetByIdWithNotExistingPlace() {
        assertEquals(mPlaceProvider.getCount(), 0);

        try {
            Place b = mPlaceProvider.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (RecordNotFoundException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(mPlaceProvider.getCount(), 0);

        long id = insertDummyPlaceDestination();

        assertEquals(mPlaceProvider.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        mPlaceProvider.deleteAll();

        assertEquals(mPlaceProvider.getCount(), 0);

        Place b = mPlaceProvider.getDummy();
        id1 = mPlaceProvider.insert(b);
        id2 = mPlaceProvider.insert(b);

        assertEquals(mPlaceProvider.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Place a = mPlaceProvider.getDummy();
        Place b = mPlaceProvider.getDummy();
        b.setName(a.getName());

        mPlaceProvider.insert(a);
        mPlaceProvider.insert(b);
    }

    public void testGetContentValues() {
        Place b = new Place();

        b.setName("5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");
        b.setLatitude(39);
        b.setLongitude(38);

        ContentValues cv = mPlaceProvider.getContentValues(b);

        assertEquals(cv.get(TablePlace.COLUMN_NAME), b.getName());
        assertEquals(cv.get(TablePlace.COLUMN_DESTINATION), b.getDestination());
        assertEquals(cv.get(TablePlace.COLUMN_LATITUDE), b.getLatitude());
        assertEquals(cv.get(TablePlace.COLUMN_LONGITUDE), b.getLongitude());
    }

    public void testDeleteAllWithNoRecords() {
        mPlaceProvider.deleteAll();
        assertEquals(mPlaceProvider.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = mPlaceProvider.getCount();
        assertNotSame(0, countBeforeAdd);

        insertDummyPlaceDestination();

        long countAfterAdd = mPlaceProvider.getCount();

        mPlaceProvider.deleteAll();

        long countDeleted = mPlaceProvider.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(mPlaceProvider.getTableName().equals(TablePlace.TABLE_NAME));
    }
}
