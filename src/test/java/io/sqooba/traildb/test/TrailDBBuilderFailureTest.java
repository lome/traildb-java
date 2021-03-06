package io.sqooba.traildb.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.sqooba.traildb.TrailDB;
import io.sqooba.traildb.TrailDBException;
import io.sqooba.traildb.TrailDBNative;
import mockit.Expectations;

public class TrailDBBuilderFailureTest {

    private final String path = "testdb";
    private final String cookie = "12345678123456781234567812345678";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @After
    public void tearDown() throws IOException {

        // Clear the TrailDB files/directories created for the tests.
        final File f = new File(this.path + ".tdb");
        if (f.exists() && !f.isDirectory()) {
            f.delete();
        }
        FileUtils.deleteDirectory(new File(this.path));
    }

    @Test
    public void consInitFailure() {

        this.expectedEx.expect(TrailDBException.class);
        this.expectedEx.expectMessage("Failed to allocate memory for constructor.");

        final TrailDBNative traildbj = TrailDBNative.INSTANCE;
        new Expectations(traildbj) {

            {
                traildbj.consInit();
                this.result = -1;
            }
        };
        new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" });
    }

    @Test
    public void consOpenFailure() throws Exception {

        this.expectedEx.expect(TrailDBException.class);
        this.expectedEx.expectMessage("Can not open constructor.");

        final TrailDBNative traildbj = TrailDBNative.INSTANCE;
        new Expectations(traildbj) {

            {
                traildbj.consOpen(this.anyLong, this.anyString, (String[])this.any, this.anyLong);
                this.result = -1;
            }
        };
        new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" });
    }

    @Test
    public void consAddFailure() {

        this.expectedEx.expect(TrailDBException.class);
        this.expectedEx.expectMessage("Failed to add: -1");

        final TrailDBNative traildbj = TrailDBNative.INSTANCE;
        new Expectations(traildbj) {

            {
                traildbj.consAdd(this.anyLong, (byte[])this.any, this.anyLong, (String[])this.any,
                        (long[])this.any);
                this.result = -1;
            }
        };
        new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" })
                .add(this.cookie, 120, new String[] { "a", "hinata" });
    }

    @Test
    public void consAppendFailure() {

        this.expectedEx.expect(TrailDBException.class);
        this.expectedEx.expectMessage("Failed to merge dbs: -1");

        final TrailDBNative traildbj = TrailDBNative.INSTANCE;
        new Expectations(traildbj) {

            {
                traildbj.consAppend(this.anyLong, this.anyLong);
                this.result = -1;
            }
        };
        final TrailDB db = new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" }).build();
        new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" }).append(db);
    }

    @Test
    public void consFinaliseFailure() {

        this.expectedEx.expect(TrailDBException.class);
        this.expectedEx.expectMessage("Failed to finalize.");

        final TrailDBNative traildbj = TrailDBNative.INSTANCE;
        new Expectations(traildbj) {

            {
                traildbj.consFinalize(this.anyLong);
                this.result = -1;
            }
        };
        new TrailDB.TrailDBBuilder(this.path, new String[] { "field1", "field2" }).build();
    }

}
