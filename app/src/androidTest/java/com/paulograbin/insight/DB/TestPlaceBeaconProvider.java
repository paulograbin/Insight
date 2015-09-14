package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.PlaceBeacon;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPlaceBeaconProvider extends ApplicationTestCase<Application> {

    PlaceBeaconProvider mPlaceBeaconProvider;

    public TestPlaceBeaconProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mPlaceBeaconProvider = new PlaceBeaconProvider(getContext());
        assertNotNull(mPlaceBeaconProvider);

        mPlaceBeaconProvider.deleteAll();
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
    }

    public long insertDummyPlaceBeacon() {
        long id;

        PlaceBeacon m = mPlaceBeaconProvider.getDummy();
        id = mPlaceBeaconProvider.insert(m);

        assertNotNull(id);
        assertNotSame(id, 0);

        return id;
    }

    public void testDeleteByPlaceBeaconExisting() {
        long id = insertDummyPlaceBeacon();
        assertEquals(mPlaceBeaconProvider.getCount(), 1);

        PlaceBeacon m = mPlaceBeaconProvider.getByID(id);

        int affectedRows = mPlaceBeaconProvider.delete(m);

        assertEquals(affectedRows, 1);
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
    }

    public void testDeleteByPlaceBeaconNotExisting() {
        PlaceBeacon b = mPlaceBeaconProvider.getDummy();
        long id = mPlaceBeaconProvider.insert(b);

        int affectedRows = mPlaceBeaconProvider.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);

        long id = insertDummyPlaceBeacon();
        assertEquals(mPlaceBeaconProvider.getCount(), 1);

        int affectedRows = mPlaceBeaconProvider.delete(id);
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
        int affetedRows = mPlaceBeaconProvider.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetByUUIDExisting() {
        PlaceBeacon pb = mPlaceBeaconProvider.getDummy();
        pb.setId(mPlaceBeaconProvider.insert(pb));

        PlaceBeacon pb2 = mPlaceBeaconProvider.getByUUID(pb.getUuid());
        assertEquals(pb.getUuid(), pb2.getUuid());
    }

    public void testGetByUUIDNonExisting() {
        try {
            PlaceBeacon pb2 = mPlaceBeaconProvider.getByUUID("blablabla");
            Assert.fail("Shoud have thrown an exception");
        } catch(RecordNotFoundException e) {

        }
    }

    public void testGetAllNoPlaceBeacon() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);

        List<PlaceBeacon> beacons = mPlaceBeaconProvider.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPlaceBeacon() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
        List<PlaceBeacon> PlaceBeacons = mPlaceBeaconProvider.getAll();

        insertDummyPlaceBeacon();
        PlaceBeacons = mPlaceBeaconProvider.getAll();
        assertEquals(PlaceBeacons.size(), mPlaceBeaconProvider.getCount());
        assertEquals(PlaceBeacons.size(), 1);

        insertDummyPlaceBeacon();
        PlaceBeacons = mPlaceBeaconProvider.getAll();
        assertEquals(PlaceBeacons.size(), mPlaceBeaconProvider.getCount());
        assertEquals(PlaceBeacons.size(), 2);

        insertDummyPlaceBeacon();
        PlaceBeacons = mPlaceBeaconProvider.getAll();
        assertEquals(PlaceBeacons.size(), mPlaceBeaconProvider.getCount());
        assertEquals(PlaceBeacons.size(), 3);

        PlaceBeacon a = PlaceBeacons.get(2);
        PlaceBeacon b = mPlaceBeaconProvider.getDummy();

        assertEquals(a.getIdPlace(), b.getIdPlace());
        assertEquals(a.getIdBeacon(), b.getIdBeacon());
        assertEquals(a.getCreatedDate(), b.getCreatedDate());
        assertEquals(a.getCreatedTime(), b.getCreatedTime());
    }

    public void testUpdateExisting() {
        PlaceBeacon a = mPlaceBeaconProvider.getDummy();
        long id = mPlaceBeaconProvider.insert(a);

        PlaceBeacon b = mPlaceBeaconProvider.getByID(id);

        long newIdPlace = 10;
        long newIdBeacon = 20;

        b.setIdPlace(newIdPlace);
        b.setIdBeacon(newIdBeacon);

        mPlaceBeaconProvider.update(b);

        PlaceBeacon c = mPlaceBeaconProvider.getByID(id);
        assertEquals(c.getIdPlace(), newIdPlace);
        assertEquals(c.getIdBeacon(), newIdBeacon);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        PlaceBeacon b = mPlaceBeaconProvider.getDummy();

        long newIdPlace = 10;
        long newIdBeacon = 20;

        b.setIdPlace(newIdPlace);
        b.setIdBeacon(newIdBeacon);

        int affectedRows = mPlaceBeaconProvider.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPlaceBeacon() {
        PlaceBeacon a = mPlaceBeaconProvider.getDummy();
        long id = mPlaceBeaconProvider.insert(a);

        assertNotSame(id, 0);
        assertEquals(mPlaceBeaconProvider.getCount(), 1);

        PlaceBeacon b = mPlaceBeaconProvider.getByID(id);
        assertNotNull(b);

        assertEquals(a.getIdPlace(), b.getIdPlace());
        assertEquals(a.getIdBeacon(), b.getIdBeacon());
        assertEquals(a.getCreatedDate(), b.getCreatedDate());
        assertEquals(a.getCreatedTime(), b.getCreatedTime());
    }

    public void testGetByIdWithNotExistingPlaceBeacon() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);

        try {
            PlaceBeacon b = mPlaceBeaconProvider.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (RecordNotFoundException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(mPlaceBeaconProvider.getCount(), 0);

        long id = insertDummyPlaceBeacon();

        assertEquals(mPlaceBeaconProvider.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        mPlaceBeaconProvider.deleteAll();

        assertEquals(mPlaceBeaconProvider.getCount(), 0);

        PlaceBeacon b = mPlaceBeaconProvider.getDummy();
        id1 = mPlaceBeaconProvider.insert(b);
        id2 = mPlaceBeaconProvider.insert(b);

        assertEquals(mPlaceBeaconProvider.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

//    public void testInsertDuplicated() {
//        PlaceBeacon a = mPlaceBeaconProvider.getDummy();
//        PlaceBeacon b = mPlaceBeaconProvider.getDummy();
//        b.setText(a.getText());
//
//        mPlaceBeaconProvider.insert(a);
//        mPlaceBeaconProvider.insert(b);
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

        ContentValues cv = mPlaceBeaconProvider.getContentValues(b);

        assertEquals(cv.get(TablePlaceBeacon.COLUMN_IDPLACE), b.getIdPlace());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_IDBEACON), b.getIdBeacon());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_CREATED_DATE), b.getCreatedDate());
        assertEquals(cv.get(TablePlaceBeacon.COLUMN_CREATED_TIME), b.getCreatedTime());
    }

    public void testDeleteAllWithNoRecords() {
        mPlaceBeaconProvider.deleteAll();
        assertEquals(mPlaceBeaconProvider.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = mPlaceBeaconProvider.getCount();

        insertDummyPlaceBeacon();

        long countAfterAdd = mPlaceBeaconProvider.getCount();

        mPlaceBeaconProvider.deleteAll();

        long countDeleted = mPlaceBeaconProvider.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(mPlaceBeaconProvider.getTableName().equals(TablePlaceBeacon.TABLE_NAME));
    }
}
