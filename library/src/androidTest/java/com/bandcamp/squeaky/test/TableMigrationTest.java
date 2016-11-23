package com.bandcamp.squeaky.test;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

import com.bandcamp.squeaky.Database;
import com.bandcamp.squeaky.Table;
import com.bandcamp.squeaky.util.Logger;

@RunWith(AndroidJUnit4.class)
public class TableMigrationTest {

    @Test
    public void testCreationAndUpgrade() {
        Database db = new Database(InstrumentationRegistry.getContext(), getClass().getSimpleName());
        TestTable t = new TestTable();
        db.addTable(t);
        assertThat(db.getTables().size()).isEqualTo(1);

        db.prepare();
        Cursor c = db.query("SELECT version FROM versions WHERE table_name = ?", t.getName());
        c.moveToNext();
        assertThat(c.getColumnCount()).isEqualTo(1);
        assertThat(c.getInt(c.getColumnIndex("version"))).isEqualTo(1);
        c.close();
        db.close();

        // Bump the version and create a new instance of the DB class, so we can have it migrate
        // the existing database from above.
        t.bumpVersion();
        db.addTable(t);
        db.prepare();

        Cursor c2 = db.query("SELECT version FROM versions WHERE table_name = ?", t.getName());
        c2.moveToNext();
        assertThat(c2.getInt(0)).isEqualTo(2);
        c2.close();

        assertThat(db.insert("INSERT INTO test_table (col1, col2, col3) VALUES (?,?,?)", 1, 2, 3)).isGreaterThan(0);

        c2 = db.query("SELECT * FROM test_table");
        c2.moveToNext();
        assertThat(c2.getInt(0)).isEqualTo(1);
        assertThat(c2.getInt(1)).isEqualTo(2);
        assertThat(c2.getInt(2)).isEqualTo(3);
        c2.close();

        Logger.dumpTables(db);
    }

    public static class TestTable extends Table {
        private int mVersion;

        private String[] migrate1to2 = new String[]{
                "ALTER TABLE test_table ADD COLUMN col3 INTEGER"
        };

        public TestTable() {
            mVersion = 1;
        }

        @Override
        public String getName() {
            return "test_table";
        }

        @Override
        public int getVersion() {
            return mVersion;
        }

        protected void bumpVersion() {
            mVersion++;
        }

        @Override
        public String[] getCreateTable() {
            return new String[] {
                    "CREATE TABLE test_table (col1 INTEGER NOT NULL, col2 INTEGER NOT NULL)",
            };
        }

        @Override
        public String[] getMigration(int nextVersion) {
            return migrate1to2;
        }
    }
}
