package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.Model.Path;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPathProvider extends ApplicationTestCase<Application> {

    PathProvider pp;

    long idDummyPlace = 1L;
    long idDummyConnectedTo = 3L;
    int weightDummy = 1;

    public TestPathProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        pp = new PathProvider(getContext());
        assertNotNull(pp);

        pp.deleteAll();
        assertEquals(pp.getCount(), 0);
    }

    public long insertDummyPath() {
        Path p = pp.getDummy();
        p.setId(pp.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public void insertTestRecords() {
        Path ph1 = new Path(10L, 20L, 1);
        Path ph3 = new Path(10L, 30L, 1);
        Path ph2 = new Path(20L, 30L, 1);
        Path ph4 = new Path(20L, 40, 1);

        ph1.setId(pp.insert(ph1));
        ph2.setId(pp.insert(ph2));
        ph3.setId(pp.insert(ph3));
        ph4.setId(pp.insert(ph4));
    }

    public void testGetAllPlaceConnections() {
        insertTestRecords();

        List<Path> paths = pp.getAllPlaceConnections(10L);
        assertEquals(paths.size(), 2);
    }

    public void testGetAllPlacesConnectedTo() {
        insertTestRecords();

        List<Path> paths = pp.getAllPlacesConnectedTo(30L);
        assertEquals(paths.size(), 2);
    }

    public void testDeleteByPathExisting() {
        long id = insertDummyPath();
        assertEquals(pp.getCount(), 1);

        Path p = pp.getByID(id);

        int affectedRows = pp.delete(p);

        assertEquals(affectedRows, 1);
        assertEquals(pp.getCount(), 0);
    }

    public void testDeleteByPathNotExisting() {
        Path b = pp.getDummy();
        long id = pp.insert(b);

        int affectedRows = pp.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(pp.getCount(), 0);

        long id = insertDummyPath();
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

    public void testGetAllNoPath() {
        assertEquals(pp.getCount(), 0);

        List<Path> beacons = pp.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPath() {
        assertEquals(pp.getCount(), 0);
        List<Path> Paths = pp.getAll();

        insertDummyPath();
        Paths = pp.getAll();
        assertEquals(Paths.size(), pp.getCount());
        assertEquals(Paths.size(), 1);

        insertDummyPath();
        Paths = pp.getAll();
        assertEquals(Paths.size(), pp.getCount());
        assertEquals(Paths.size(), 2);

        insertDummyPath();
        Paths = pp.getAll();
        assertEquals(Paths.size(), pp.getCount());
        assertEquals(Paths.size(), 3);

        Path a = Paths.get(2);
        Path b = pp.getDummy();

        assertEquals(a.getPlace(), b.getPlace());
        assertEquals(a.getConnectedTo(), b.getConnectedTo());
        assertEquals(a.getWeight(), b.getWeight());
    }

    public void testUpdateExisting() {
        Path a = pp.getDummy();
        a.setId(pp.insert(a));

        Path b = pp.getByID(a.getId());

        int newWeight = 999;
        b.setWeight(newWeight);

        pp.update(b);

        Path c = pp.getByID(a.getId());
        assertEquals(c.getWeight(), newWeight);
        assertEquals(c.getId(), a.getId());
    }

    public void testUpdateNotExisting() {
        Path b = pp.getDummy();

        int affectedRows = pp.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPath() {
        Path a = pp.getDummy();
        a.setId(pp.insert(a));

        assertNotSame(a.getId(), 0);
        assertEquals(pp.getCount(), 1);

        Path b = pp.getByID(a.getId());
        assertNotNull(b);

        assertTrue(a.isEqualTo(b));
    }

    public void testGetByIdWithNotExistingPath() {
        assertEquals(pp.getCount(), 0);

        try {
            Path b = pp.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (SQLiteException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(pp.getCount(), 0);

        long id = insertDummyPath();

        assertEquals(pp.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        pp.deleteAll();

        assertEquals(pp.getCount(), 0);

        Path b = pp.getDummy();
        id1 = pp.insert(b);
        id2 = pp.insert(b);

        assertEquals(pp.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Path a = pp.getDummy();
        Path b = pp.getDummy();

        pp.insert(a);
        pp.insert(b);

        // TODO: make it no allowed to insert duplicate records
    }

    public void testGetContentValues() {
        Path p = new Path();

        p.setPlace(idDummyPlace);
        p.setConnectedTo(idDummyConnectedTo);
        p.setWeight(weightDummy);

        ContentValues cv = pp.getContentValues(p);

        assertEquals(cv.get(TablePath.COLUMN_PLACE), p.getPlace());
        assertEquals(cv.get(TablePath.COLUMN_CONNECTED_TO), p.getConnectedTo());
        assertEquals(cv.get(TablePath.COLUMN_WEIGHT), p.getWeight());
    }

    public void testDeleteAllWithNoRecords() {
        pp.deleteAll();
        assertEquals(pp.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = pp.getCount();

        insertDummyPath();

        long countAfterAdd = pp.getCount();

        pp.deleteAll();

        long countDeleted = pp.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(pp.getTableName().equals(TablePath.TABLE_NAME));
    }
}
