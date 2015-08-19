package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Model.PlaceBeacon;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPlaceBeaconProvider extends ApplicationTestCase<Application> {

    PlaceBeaconProvider pbp;

    public TestPlaceBeaconProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        pbp = new PlaceBeaconProvider(getContext());
        assertNotNull(pbp);

        pbp.deleteAll();
        assertEquals(pbp.getCount(), 0);
    }

    public long insertDummyPlaceBeacon() {
        long id;

        PlaceBeacon m = pbp.getDummy();
        id = pbp.insert(m);

        assertNotNull(id);
        assertNotSame(id, 0);

        return id;
    }

    public void testDeleteByPlaceBeaconExisting() {
        long id = insertDummyPlaceBeacon();
        assertEquals(pbp.getCount(), 1);

        PlaceBeacon m = pbp.getByID(id);

        int affectedRows = pbp.delete(m);

        assertEquals(affectedRows, 1);
        assertEquals(pbp.getCount(), 0);
    }

    public void testDeleteByPlaceBeaconNotExisting() {
        PlaceBeacon b = pbp.getDummy();
        long id = pbp.insert(b);

        int affectedRows = pbp.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(pbp.getCount(), 0);

        long id = insertDummyPlaceBeacon();
        assertEquals(pbp.getCount(), 1);

        int affectedRows = pbp.delete(id);
        assertEquals(pbp.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(pbp.getCount(), 0);
        int affetedRows = pbp.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoPlaceBeacon() {
        assertEquals(pbp.getCount(), 0);

        List<PlaceBeacon> beacons = pbp.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPlaceBeacon() {
        assertEquals(pbp.getCount(), 0);
        List<PlaceBeacon> PlaceBeacons = pbp.getAll();

        insertDummyPlaceBeacon();
        PlaceBeacons = pbp.getAll();
        assertEquals(PlaceBeacons.size(), pbp.getCount());
        assertEquals(PlaceBeacons.size(), 1);

        insertDummyPlaceBeacon();
        PlaceBeacons = pbp.getAll();
        assertEquals(PlaceBeacons.size(), pbp.getCount());
        assertEquals(PlaceBeacons.size(), 2);

        insertDummyPlaceBeacon();
        PlaceBeacons = pbp.getAll();
        assertEquals(PlaceBeacons.size(), pbp.getCount());
        assertEquals(PlaceBeacons.size(), 3);

        PlaceBeacon a = PlaceBeacons.get(2);
        PlaceBeacon b = pbp.getDummy();

        assertEquals(a.getIdPlace(), b.getIdPlace());
        assertEquals(a.getIdBeacon(), b.getIdBeacon());
        assertEquals(a.getCreatedDate(), b.getCreatedDate());
        assertEquals(a.getCreatedTime(), b.getCreatedTime());
    }

    public void testUpdateExisting() {
        PlaceBeacon a = pbp.getDummy();
        long id = pbp.insert(a);

        PlaceBeacon b = pbp.getByID(id);

        long newIdPlace = 10;
        long newIdBeacon = 20;

        b.setIdPlace(newIdPlace);
        b.setIdBeacon(newIdBeacon);

        pbp.update(b);

        PlaceBeacon c = pbp.getByID(id);
        assertEquals(c.getIdPlace(), newIdPlace);
        assertEquals(c.getIdBeacon(), newIdBeacon);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        PlaceBeacon b = pbp.getDummy();

        long newIdPlace = 10;
        long newIdBeacon = 20;

        b.setIdPlace(newIdPlace);
        b.setIdBeacon(newIdBeacon);

        int affectedRows = pbp.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPlaceBeacon() {
        PlaceBeacon a = pbp.getDummy();
        long id = pbp.insert(a);

        assertNotSame(id, 0);
        assertEquals(pbp.getCount(), 1);

        PlaceBeacon b = pbp.getByID(id);
        assertNotNull(b);

        assertEquals(a.getIdPlace(), b.getIdPlace());
        assertEquals(a.getIdBeacon(), b.getIdBeacon());
        assertEquals(a.getCreatedDate(), b.getCreatedDate());
        assertEquals(a.getCreatedTime(), b.getCreatedTime());
    }

    public void testGetByIdWithNotExistingPlaceBeacon() {
        assertEquals(pbp.getCount(), 0);

        try {
            PlaceBeacon b = pbp.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (SQLiteException e) {

        }
    }

    public void testInsertOne() {
        assertEquals(pbp.getCount(), 0);

        long id = insertDummyPlaceBeacon();

        assertEquals(pbp.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        pbp.deleteAll();

        assertEquals(pbp.getCount(), 0);

        PlaceBeacon b = pbp.getDummy();
        id1 = pbp.insert(b);
        id2 = pbp.insert(b);

        assertEquals(pbp.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

//    public void testInsertDuplicated() {
//        PlaceBeacon a = pbp.getDummy();
//        PlaceBeacon b = pbp.getDummy();
//        b.setText(a.getText());
//
//        pbp.insert(a);
//        pbp.insert(b);
//    }

    public void testGetContentValues() {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        PlaceBeacon b = new PlaceBeacon();

        b.setId(5L);
        b.setIdPlace(10);
        b.setIdBeacon(27);
        b.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        ContentValues cv = pbp.getContentValues(b);

        assertEquals(cv.get(TablePlaceBeacon.COLUMN_IDPLACE), b.getIdPlace());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_IDBEACON), b.getIdBeacon());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_CREATED_DATE), b.getCreatedDate());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_CREATED_TIME), b.getCreatedTime());
    }

    public void testDeleteAllWithNoRecords() {
        pbp.deleteAll();
        assertEquals(pbp.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = pbp.getCount();

        insertDummyPlaceBeacon();

        long countAfterAdd = pbp.getCount();

        pbp.deleteAll();

        long countDeleted = pbp.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(pbp.getTableName().equals(TablePlaceBeacon.TABLE_NAME));
    }
}
