package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.Exceptions.RecordNotFoundException;
import com.paulograbin.insight.Model.Path;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestPathProvider extends ApplicationTestCase<Application> {

    PathProvider mPathProvider;

    long idDummyPlace = 1L;
    long idDummyConnectedTo = 3L;
    int weightDummy = 1;

    public TestPathProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mPathProvider = new PathProvider(getContext());
        assertNotNull(mPathProvider);

        mPathProvider.deleteAll();
        assertEquals(mPathProvider.getCount(), 0);
    }

    public long insertDummyPath() {
        Path p = mPathProvider.getDummy();
        p.setId(mPathProvider.insert(p));

        assertNotNull(p.getId());
        assertNotSame(p.getId(), 0);

        return p.getId();
    }

    public void insertTestRecords() {
        Path ph1 = new Path(10L, 20L, 1);
        Path ph3 = new Path(10L, 30L, 1);
        Path ph2 = new Path(20L, 30L, 1);
        Path ph4 = new Path(20L, 40, 1);

        ph1.setId(mPathProvider.insert(ph1));
        ph2.setId(mPathProvider.insert(ph2));
        ph3.setId(mPathProvider.insert(ph3));
        ph4.setId(mPathProvider.insert(ph4));
    }

    public void testGetAllPlaceConnections() {
        insertTestRecords();

        List<Path> paths = mPathProvider.getAllPlaceConnections(10L);
        assertEquals(paths.size(), 2);
    }

    public void testGetAllPlacesConnectedTo() {
        insertTestRecords();

        List<Path> paths = mPathProvider.getAllPlacesConnectedTo(30L);
        assertEquals(paths.size(), 2);
    }

    public void testDeleteByPathExisting() {
        try {
            long id = insertDummyPath();
            assertEquals(mPathProvider.getCount(), 1);

            Path p = mPathProvider.getByID(id);

            int affectedRows = mPathProvider.delete(p);

            assertEquals(affectedRows, 1);
            assertEquals(mPathProvider.getCount(), 0);
        } catch(RecordNotFoundException e) {
            Assert.fail();
        }
    }

    public void testDeleteByPathNotExisting() {
        Path b = mPathProvider.getDummy();
        long id = mPathProvider.insert(b);

        int affectedRows = mPathProvider.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(mPathProvider.getCount(), 0);

        long id = insertDummyPath();
        assertEquals(mPathProvider.getCount(), 1);

        int affectedRows = mPathProvider.delete(id);
        assertEquals(mPathProvider.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(mPathProvider.getCount(), 0);
        int affetedRows = mPathProvider.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoPath() {
        assertEquals(mPathProvider.getCount(), 0);

        List<Path> beacons = mPathProvider.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyPath() {
        assertEquals(mPathProvider.getCount(), 0);
        List<Path> Paths = mPathProvider.getAll();

        insertDummyPath();
        Paths = mPathProvider.getAll();
        assertEquals(Paths.size(), mPathProvider.getCount());
        assertEquals(Paths.size(), 1);

        insertDummyPath();
        Paths = mPathProvider.getAll();
        assertEquals(Paths.size(), mPathProvider.getCount());
        assertEquals(Paths.size(), 2);

        insertDummyPath();
        Paths = mPathProvider.getAll();
        assertEquals(Paths.size(), mPathProvider.getCount());
        assertEquals(Paths.size(), 3);

        Path a = Paths.get(2);
        Path b = mPathProvider.getDummy();

        assertEquals(a.getPlace(), b.getPlace());
        assertEquals(a.getConnectedTo(), b.getConnectedTo());
        assertEquals(a.getWeight(), b.getWeight());
    }

    public void testUpdateExisting() {
        try {
            Path a = mPathProvider.getDummy();
            a.setId(mPathProvider.insert(a));

            Path b = mPathProvider.getByID(a.getId());

            int newWeight = 999;
            b.setWeight(newWeight);

            mPathProvider.update(b);

            Path c = mPathProvider.getByID(a.getId());
            assertEquals(c.getWeight(), newWeight);
            assertEquals(c.getId(), a.getId());
        } catch (RecordNotFoundException e) {
            Assert.fail();
        }
    }

    public void testUpdateNotExisting() {
        Path b = mPathProvider.getDummy();

        int affectedRows = mPathProvider.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingPath() {
        try {
            Path a = mPathProvider.getDummy();
            a.setId(mPathProvider.insert(a));

            assertNotSame(a.getId(), 0);
            assertEquals(mPathProvider.getCount(), 1);

            Path b = mPathProvider.getByID(a.getId());
            assertNotNull(b);

            assertTrue(a.isEqualTo(b));
        } catch (RecordNotFoundException e) {
            Assert.fail();
        }
    }

    public void testGetByIdWithNotExistingPath() {
        assertEquals(mPathProvider.getCount(), 0);

        try {
            Path b = mPathProvider.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (RecordNotFoundException ignored) {

        }
    }

    public void testInsertOne() {
        assertEquals(mPathProvider.getCount(), 0);

        long id = insertDummyPath();

        assertEquals(mPathProvider.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        mPathProvider.deleteAll();

        assertEquals(mPathProvider.getCount(), 0);

        Path b = mPathProvider.getDummy();
        id1 = mPathProvider.insert(b);
        id2 = mPathProvider.insert(b);

        assertEquals(mPathProvider.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Path a = mPathProvider.getDummy();
        Path b = mPathProvider.getDummy();

        mPathProvider.insert(a);
        mPathProvider.insert(b);
    }

    public void testGetContentValues() {
        Path p = new Path();

        p.setPlace(idDummyPlace);
        p.setConnectedTo(idDummyConnectedTo);
        p.setWeight(weightDummy);

        ContentValues cv = mPathProvider.getContentValues(p);

        assertEquals(cv.get(TablePath.COLUMN_PLACE), p.getPlace());
        assertEquals(cv.get(TablePath.COLUMN_CONNECTED_TO), p.getConnectedTo());
        assertEquals(cv.get(TablePath.COLUMN_WEIGHT), p.getWeight());
    }

    public void testDeleteAllWithNoRecords() {
        mPathProvider.deleteAll();
        assertEquals(mPathProvider.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = mPathProvider.getCount();

        insertDummyPath();

        long countAfterAdd = mPathProvider.getCount();

        mPathProvider.deleteAll();

        long countDeleted = mPathProvider.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(mPathProvider.getTableName().equals(TablePath.TABLE_NAME));
    }
}
