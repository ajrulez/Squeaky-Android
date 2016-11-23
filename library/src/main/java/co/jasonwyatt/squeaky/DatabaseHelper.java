package co.jasonwyatt.squeaky;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.jasonwyatt.squeaky.util.Logger;

/**
 * Base class used by {@link Database} for managing a connection to an SQLite database.<br/><br/>
 *
 * You can extend this class if you want to perform additional functionality after {@link Database}
 * does its migrations. Be sure to call the super method at the beginning of your DatabaseHelper
 * subclass's methods.
 * @see android.database.sqlite.SQLiteOpenHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.d("DatabaseHelper.onCreate(",db,")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.d("DatabaseHelper.onUpgrade(",db,",",oldVersion,",",newVersion,")");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.d("DatabaseHelper.onDowngrade(",db,",",oldVersion,",",newVersion,")");
    }
}
