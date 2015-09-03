package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.Model.Place;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPlaceProvider extends ApplicationTestCase<Application> {

    PlaceProvider pp;

    public TestPlaceProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        pp = new PlaceProvider(getContext());
        assertNotNull(pp);

        pp.deleteAll();
        assertEquals(pp.getCount(), 0);
    }

    public long insertDummyPlaceDestination() {
        Place p = pp.getDummy();
        p.setId(pp.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public long insertDummyPlaceNonDestination() {
        Place p = pp.getDummy();
        p.setDestination(0);

        p.setId(pp.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public void testDeleteByPlaceExisting() {
        long id = insertDummyPlaceDestination();
        assertEquals(pp.getCount(), 1);

        Place m = pp.getByID(id);

        int affectedRows = pp.delete(m);

        assertEquals(affectedRows, 1);
        assertEquals(pp.getCount(), 0);
    }

    public void testDeleteByPlaceNotExisting() {
        Place b = pp.getDummy();
        long id = pp.insert(b);

        int affectedRows = pp.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(pp.getCount(), 0);

        long id = insertDummyPlaceDestination();
        assertEquals(pp.getCount(), 1);

        int affectedRows = pp.delete(id);
        assertEquals(pp.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(pp.getCount(), 0);
        int affetedRows = pp.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoPlace() {
        assertEquals(pp.getCount(), 0);

        List<Place> beacons = pp.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPlace() {
        assertEquals(pp.getCount(), 0);
        List<Place> Places = pp.getAll();

        insertDummyPlaceDestination();
        Places = pp.getAll();
        assertEquals(Places.size(), pp.getCount());
        assertEquals(Places.size(), 1);

        insertDummyPlaceDestination();
        Places = pp.getAll();
        assertEquals(Places.size(), pp.getCount());
        assertEquals(Places.size(), 2);

        insertDummyPlaceDestination();
        Places = pp.getAll();
        assertEquals(Places.size(), pp.getCount());
        assertEquals(Places.size(), 3);

        Place a = Places.get(2);
        Place b = pp.getDummy();

        assertEquals(a.getName(), b.getName());
    }

    public void testGetAllDestination() {
        insertDummyPlaceNonDestination();

        insertDummyPlaceDestination();
        assertEquals(pp.getAllDestinationPlaces().size(), pp.getCount() - 1);
    }

    public void testGetAllNonDestination() {
        insertDummyPlaceDestination();

        insertDummyPlaceNonDestination();
        assertEquals(pp.getAllNonDestinationPlaces().size(), pp.getCount() - 1);
    }

    public void testUpdateExisting() {
        Place a = pp.getDummy();
        long id = pp.insert(a);

        Place b = pp.getByID(id);
        String newText = "123456789";
        b.setName(newText);

        pp.update(b);

        Place c = pp.getByID(id);
        assertEquals(c.getName(), newText);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        Place b = pp.getDummy();

        String newText = "123456789";
        b.setName(newText);

        int affectedRows = pp.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPlace() {
        Place a = pp.getDummy();
        long id = pp.insert(a);

        assertNotSame(id, 0);
        assertEquals(pp.getCount(), 1);

        Place b = pp.getByID(id);
        assertNotNull(b);

        assertEquals(a.getName(), b.getName());
    }

    public void testGetByIdWithNotExistingPlace() {
        assertEquals(pp.getCount(), 0);

        try {
            Place b = pp.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (SQLiteException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(pp.getCount(), 0);

        long id = insertDummyPlaceDestination();

        assertEquals(pp.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        pp.deleteAll();

        assertEquals(pp.getCount(), 0);

        Place b = pp.getDummy();
        id1 = pp.insert(b);
        id2 = pp.insert(b);

        assertEquals(pp.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Place a = pp.getDummy();
        Place b = pp.getDummy();
        b.setName(a.getName());

        pp.insert(a);
        pp.insert(b);
    }

    public void testGetContentValues() {
        Place b = new Place();

        b.setName("5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");
        b.setLatitude(39);
        b.setLongitude(38);

        ContentValues cv = pp.getContentValues(b);

        assertEquals(cv.get(TablePlace.COLUMN_NAME), b.getName());
        assertEquals(cv.get(TablePlace.COLUMN_DESTINATION), b.getDestination());
        assertEquals(cv.get(TablePlace.COLUMN_LATITUDE), b.getLatitude());
        assertEquals(cv.get(TablePlace.COLUMN_LONGITUDE), b.getLongitude());
    }

    public void testDeleteAllWithNoRecords() {
        pp.deleteAll();
        assertEquals(pp.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = pp.getCount();

        insertDummyPlaceDestination();

        long countAfterAdd = pp.getCount();

        pp.deleteAll();

        long countDeleted = pp.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(pp.getTableName().equals(TablePlace.TABLE_NAME));
    }
}
