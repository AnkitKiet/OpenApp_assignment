package edu.openapp.global;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import edu.openapp.dao.MemberDao;
import edu.openapp.model.MemberModel;

/**
 * Created by Ankit on 19/01/18.
 */

@Database(entities = {MemberModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "aadhar_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract MemberDao itemAndPersonModel();

}
