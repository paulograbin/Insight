package com.paulograbin.insight.DB.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.paulograbin.insight.DB.Table.TableMessage;
import com.paulograbin.insight.Model.Message;

/**
 * Created by paulograbin on 24/07/15.
 */
public class MessageProvider extends AbstractProvider<Message> {

    public MessageProvider(Context context) {
        super(context);
    }


    @Override
    public String getTableName() {
        return TableMessage.TABLE_NAME;
    }

    @Override
    public Message getDummy() {
        Message m = new Message();

        m.setText("Você chegou ao seu destino.");

        return m;
    }

    @Override
    public ContentValues getContentValues(Message message) {
        printToLog("Obtendo contentValues da message " + message.getId());

        ContentValues cv = new ContentValues();
        cv.put(TableMessage.COLUMN_TEXT, message.getText());

        return cv;
    }

    @Override
    public Message getFromCursor(Cursor c) {
        if (c == null) {
            return null;
        }

        Message m = new Message();
        int i = 0;

        m.setId(c.getLong(i++));
        m.setText(c.getString(i++));

        return m;
    }
}