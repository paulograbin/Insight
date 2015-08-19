package com.paulograbin.insight.DB;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.MessageProvider;
import com.paulograbin.insight.DB.Table.TableMessage;
import com.paulograbin.insight.Model.Message;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by paulograbin on 06/08/15.
 */
public class TestMessageProvider extends ApplicationTestCase<Application> {

    MessageProvider mp;

    public TestMessageProvider() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mp = new MessageProvider(getContext());
        assertNotNull(mp);

        mp.deleteAll();
        assertEquals(mp.getCount(), 0);
    }

    public long insertDummyMessage() {
        long id;

        Message m = mp.getDummy();
        id = mp.insert(m);

        assertNotNull(id);
        assertNotSame(id, 0);

        return id;
    }

    public void testDeleteByMessageExisting() {
        long id = insertDummyMessage();
        assertEquals(mp.getCount(), 1);

        Message m = mp.getByID(id);

        int affectedRows = mp.delete(m);

        assertEquals(affectedRows, 1);
        assertEquals(mp.getCount(), 0);
    }

    public void testDeleteByMessageNotExisting() {
        Message b = mp.getDummy();
        long id = mp.insert(b);

        int affectedRows = mp.delete(b);

        assertEquals(affectedRows, 0);
    }

    public void testDeleteByIDExisting() {
        assertEquals(mp.getCount(), 0);

        long id = insertDummyMessage();
        assertEquals(mp.getCount(), 1);

        int affectedRows = mp.delete(id);
        assertEquals(mp.getCount(), 0);
        assertEquals(affectedRows, 1);
    }

    public void testDeleteByIDNotExisting() {
        assertEquals(mp.getCount(), 0);
        int affetedRows = mp.delete(50);

        assertEquals(affetedRows, 0);
    }

    public void testGetAllNoMessage() {
        assertEquals(mp.getCount(), 0);

        List<Message> beacons = mp.getAll();
        assertEquals(beacons.size(), 0);
    }

    public void testGetAllManyMessage() {
        assertEquals(mp.getCount(), 0);
        List<Message> messages = mp.getAll();

        insertDummyMessage();
        messages = mp.getAll();
        assertEquals(messages.size(), mp.getCount());
        assertEquals(messages.size(), 1);

        insertDummyMessage();
        messages = mp.getAll();
        assertEquals(messages.size(), mp.getCount());
        assertEquals(messages.size(), 2);

        insertDummyMessage();
        messages = mp.getAll();
        assertEquals(messages.size(), mp.getCount());
        assertEquals(messages.size(), 3);

        Message a = messages.get(2);
        Message b = mp.getDummy();

        assertEquals(a.getText(), b.getText());
    }

    public void testUpdateExisting() {
        Message a = mp.getDummy();
        long id = mp.insert(a);

        Message b = mp.getByID(id);
        String newText = "123456789";
        b.setText(newText);

        mp.update(b);

        Message c = mp.getByID(id);
        assertEquals(c.getText(), newText);
        assertEquals(c.getId(), id);
    }

    public void testUpdateNotExisting() {
        Message b = mp.getDummy();

        String newText = "123456789";
        b.setText(newText);

        int affectedRows = mp.update(b);
        assertEquals(affectedRows, 0);
    }

    public void testGetByIDWithExistingMessage() {
        Message a = mp.getDummy();
        long id = mp.insert(a);

        assertNotSame(id, 0);
        assertEquals(mp.getCount(), 1);

        Message b = mp.getByID(id);
        assertNotNull(b);

        assertEquals(a.getText(), b.getText());
    }

    public void testGetByIdWithNotExistingMessage() {
        assertEquals(mp.getCount(), 0);

        try {
            Message b = mp.getByID(50);
            Assert.fail("Should've thrown an exception...");
        } catch (SQLiteException e) {

        }
    }

    public void testInsertOne() {
        assertEquals(mp.getCount(), 0);

        long id = insertDummyMessage();

        assertEquals(mp.getCount(), 1);
        assertNotNull(id);
    }

    public void testInsertTwo() {
        long id1, id2;

        mp.deleteAll();

        assertEquals(mp.getCount(), 0);

        Message b = mp.getDummy();
        id1 = mp.insert(b);
        id2 = mp.insert(b);

        assertEquals(mp.getCount(), 2);
        assertNotNull(id1);
        assertNotNull(id2);
    }

    public void testInsertDuplicated() {
        Message a = mp.getDummy();
        Message b = mp.getDummy();
        b.setText(a.getText());

        mp.insert(a);
        mp.insert(b);
    }

    public void testGetContentValues() {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

        Message b = new Message();

        b.setText("5D8DE2E5-2C6D-4F3D-8651-DD66B7E4BD3E");

        ContentValues cv = mp.getContentValues(b);

        assertEquals(cv.get(TableMessage.COLUMN_TEXT), b.getText());
    }

    public void testDeleteAllWithNoRecords() {
        mp.deleteAll();
        assertEquals(mp.getCount(), 0);
    }

    public void testDeleteAllWithRecords() {
        long countBeforeAdd = mp.getCount();

        insertDummyMessage();

        long countAfterAdd = mp.getCount();

        mp.deleteAll();

        long countDeleted = mp.getCount();

        assertEquals(countDeleted, 0);
        assertNotSame(countDeleted, countAfterAdd);
        assertNotSame(countAfterAdd, countBeforeAdd);
    }

    public void testGetTableName() throws Exception {
        assertTrue(mp.getTableName().equals(TableMessage.TABLE_NAME));
    }
}
