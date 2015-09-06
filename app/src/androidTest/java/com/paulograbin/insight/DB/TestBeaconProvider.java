package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.Beacon;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by paulograbin on 25/07/15.
 */
public class TestBeaconProvider extends ApplicationTestCase<Application> {

    BeaconProvider mBeaconProvider;

    public TestBeaconProvider() {
        super(Application.class);
    }

    public long insertDummyBeacon() {
        long id;

        Beacon b = mBeaconProvider.getDummy();
        id = mBeaconProvider.insert(b);

        assertNotNull(id);
        assertNotSame(id, 0);

        return id;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mBeaconProvider = new BeaconProvider(getContext());
        assertNotNull(mBeaconProvider);

        mBeaconProvider.deleteAll();
        assertEquals(mBeaconProvider.getCount(), 0);
    }

    public void testDeleteByBeaconExisting() {
        long id = insertDummyBeacon();
        assertEquals(mBeaconProvider.getCount(), 1);

        Beacon b = mBeaconProvider.getByID(id);

        int affectedRows = mBeaconProvider.delete(b);

        assertEquals(affectedRows, 1);
        assertEquals(mBeaconProvider.getCount(), 0);
    }

    public void testDeleteByBeaconNotExisting() {
        Beacon b = mBeaconProvider.getDummy();
        long id = mBeaconProvider.insert(b);

        int affectedRows = mBeaconProvider.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(mBeaconProvider.getCount(), 0);

        long id = insertDummyBeacon();
        assertEquals(mBeaconProvider.getCount(), 1);

        int affectedRows = mBeaconProvider.delete(id);
        assertEquals(mBeaconProvider.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(mBeaconProvider.getCount(), 0);
        int affetedRows = mBeaconProvider.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoBeacon() {
        assertEquals(mBeaconProvider.getCount(), 0);

        List<Beacon> beacons = mBeaconProvider.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyBeacon() {
        List<Beacon> beacons = mBeaconProvider.getAll();

        insertDummyBeacon();
        beacons = mBeaconProvider.getAll();
        assertEquals(beacons.size(), mBeaconProvider.getCount());
        assertEquals(beacons.size(), 1);

        insertDummyBeacon();
        beacons = mBeaconProvider.getAll();
        assertEquals(beacons.size(), mBeaconProvider.getCount());
        assertEquals(beacons.size(), 2);

        insertDummyBeacon();
        beacons = mBeaconProvider.getAll();
        assertEquals(beacons.size(), mBeaconProvider.getCount());
        assertEquals(beacons.size(), 3);
    }

    public void testUpdateExisting() {
        Beacon a = mBeaconProvider.getDummy();
        long id = mBeaconProvider.insert(a);

        Beacon b = mBeaconProvider.getByID(id);
        String novoUUID = "123456789";
        b.setUUID(novoUUID);

        mBeaconProvider.update(b);

        Beacon c = mBeaconProvider.getByID(id);
        assertEquals(c.getUUID(), novoUUID);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        Beacon b = mBeaconProvider.getDummy();

        String novoUUID = "123456789";
        b.setUUID(novoUUID);

        int affectedRows = mBeaconProvider.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingBeacon() {
        Beacon a = mBeaconProvider.getDummy();
        long id = mBeaconProvider.insert(a);

        assertNotSame(id, 0);
        assertEquals(mBeaconProvider.getCount(), 1);

        Beacon b = mBeaconProvider.getByID(id);
        assertNotNull(b);

        assertEquals(a.getUUID(), b.getUUID());
    }

    public void testGetByIdWithNotExistingBeacon() {
        assertEquals(mBeaconProvider.getCount(), 0);

        try {
            Beacon b = mBeaconProvider.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (RecordNotFoundException ignored) {

        }
    }

    public void testGetByUUIDWithExistingBeacon() {
        String uuid = "123456789-123456789-123456789";

        Beacon b = mBeaconProvider.getDummy();
        b.setUUID(uuid);

        mBeaconProvider.insert(b);

        Beacon a = mBeaconProvider.getByUUID(uuid);
        assertEquals(a.getUUID(), uuid);
    }

    public void testGetByUUIDWithNonExistingBeacon() {
        String uuid = "123456789-123456789-123456789";

        try {
            Beacon b = mBeaconProvider.getByUUID(uuid);
            Assert.fail("Should've thrown an exception");
        } catch (RecordNotFoundException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(mBeaconProvider.getCount(), 0);

        long id = insertDummyBeacon();

        assertEquals(mBeaconProvider.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        mBeaconProvider.deleteAll();

        assertEquals(mBeaconProvider.getCount(), 0);

        Beacon b = mBeaconProvider.getDummy();
        id1 = mBeaconProvider.insert(b);
        id2 = mBeaconProvider.insert(b);

        assertEquals(mBeaconProvider.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Beacon a = mBeaconProvider.getDummy();
        Beacon b = mBeaconProvider.getDummy();
        b.setUUID(a.getUUID());

        mBeaconProvider.insert(a);
        mBeaconProvider.insert(b);
    }

    public void testGetContentValues() {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        Beacon b = new Beacon();

        b.setUUID("5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");
        b.setNetworktype(12);
        b.setMajor(12);
        b.setMajor(1);
        b.setChannel(13);
        b.setLatitude(39.99);
        b.setLongitude(30.00);
        b.setLocation("Teste de location");
        b.setCreatedDate(formatDate.format(Calendar.getInstance().getTime()));
        b.setCreatedTime(formatTime.format(Calendar.getInstance().getTime()));

        ContentValues cv = mBeaconProvider.getContentValues(b);

        assertEquals(cv.get(TableBeacon.COLUMN_UUID), b.getUUID());
        assertEquals(cv.get(TableBeacon.COLUMN_LOCATION), b.getLocation());
        assertEquals(cv.get(TableBeacon.COLUMN_LATITUDE), b.getLatitude());
        assertEquals(cv.get(TableBeacon.COLUMN_LONGITUDE), b.getLongitude());
        assertEquals(cv.get(TableBeacon.COLUMN_CREATED_DATE), b.getCreatedDate());
        assertEquals(cv.get(TableBeacon.COLUMN_CREATED_TIME), b.getCreatedTime());
        assertEquals(cv.get(TableBeacon.COLUMN_NAME), b.getName());
        assertEquals(cv.get(TableBeacon.COLUMN_NETWORKTYPE), b.getNetworktype());
        assertEquals(cv.get(TableBeacon.COLUMN_MAJOR), b.getMajor());
        assertEquals(cv.get(TableBeacon.COLUMN_MINOR), b.getMinor());
        assertEquals(cv.get(TableBeacon.COLUMN_CHANNEL), b.getChannel());
        assertEquals(cv.get(TableBeacon.COLUMN_CREATED_DATE), b.getCreatedDate());
        assertEquals(cv.get(TableBeacon.COLUMN_CREATED_TIME), b.getCreatedTime());
    }

    public void testDeleteAllWithNoRecords() {
        mBeaconProvider.deleteAll();
        assertEquals(mBeaconProvider.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = mBeaconProvider.getCount();

        insertDummyBeacon();

        long countAfterAdd = mBeaconProvider.getCount();

        mBeaconProvider.deleteAll();

        long countDeleted = mBeaconProvider.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(mBeaconProvider.getTableName().equals(TableBeacon.TABLE_NAME));
    }
}