package com.paulograbin.insight.Model;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.paulograbin.insight.DB.Provider.PathProvider;

/**
 * Created by paulograbin on 13/08/15.
 */
public class TestPath extends ApplicationTestCase<Application> {

    long idDummyPlace = 1L;
    long idDummyConnectedTo = 3L;
    int weightDummy = 1;

    public TestPath(Class<Application> applicationClass) {
        super(applicationClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Path p = getDummyPath();
        assertNotNull(p);
    }

    private Path getDummyPath() {
        PathProvider pp = new PathProvider(getContext());
        return pp.getDummy();
    }

    public void testGetId() {
        Path p = getDummyPath();

        long id = p.getId();

        assertEquals(idDummyPlace, p.getId());
    }

    public void testSetId() {

    }
}
