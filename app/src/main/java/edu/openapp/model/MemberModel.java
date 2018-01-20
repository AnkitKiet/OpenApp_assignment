package edu.openapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

import edu.openapp.global.DateConverter;

/**
 * Created by Ankit on 19/01/18.
 */

@Entity
public class MemberModel implements Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String name;
    private String address;
    @TypeConverters(DateConverter.class)
    private Date timestamp;
    @TypeConverters(DateConverter.class)
    private Date dob;
    private String image;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MemberModel createFromParcel(Parcel in) {
            return new MemberModel(in);
        }

        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };


    public MemberModel(String name, String address, Date timestamp, String image, Date dob) {
        this.name = name;
        this.address = address;
        this.timestamp = timestamp;
        this.image = image;
        this.dob = dob;
    }

    public MemberModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.timestamp = (Date) in.readSerializable();
        this.image = in.readString();
        this.dob = (Date) in.readSerializable();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getImage() {
        return image;
    }

    public Date getDob() {
        return dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeSerializable(this.timestamp);
        dest.writeString(this.image);
        dest.writeSerializable(this.dob);
    }
}


