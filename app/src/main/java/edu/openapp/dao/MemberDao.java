package edu.openapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.openapp.model.MemberModel;
import edu.openapp.global.DateConverter;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Ankit on 19/01/18.
 */


@Dao
public interface MemberDao {

    @TypeConverters(DateConverter.class)

    @Query("select * from MemberModel")
    List<MemberModel> getAllMember();

    @Query("select * from MemberModel where id = :id")
    MemberModel getItembyId(String id);

    @Query("select id,name from MemberModel")
    List<MemberModel> getAllImage();

    @Insert(onConflict = REPLACE)
    void addMember(MemberModel memberModel);

    @Update
    void updateMember(MemberModel memberModel);


}
