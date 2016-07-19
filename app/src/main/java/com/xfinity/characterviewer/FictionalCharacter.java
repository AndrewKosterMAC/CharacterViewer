package com.xfinity.characterviewer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FictionalCharacter implements Serializable, Parcelable
{
    public String getName()
    {
        return name;
    }

    private String name;

    public void setName(String value)
    {
        name = value;
    }

    public String getDescription()
    {
        return description;
    }

    private String description;

    public void setDescription(String value)
    {
        description = value;
    }

    public String getIconUrl()
    {
        return iconUrl;
    }

    private String iconUrl;

    public void setIconUrl(String value)
    {
        iconUrl = value;
    }

    public String getPageUrl()
    {
        return pageUrl;
    }

    private String pageUrl;

    public void setPageUrl(String value)
    {
        pageUrl = value;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i)
    {
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeString(getIconUrl());
        dest.writeString(getPageUrl());
    }

    public static final Parcelable.Creator<FictionalCharacter> CREATOR =
        new Parcelable.Creator<FictionalCharacter>()
        {
            @Override
            public FictionalCharacter createFromParcel(Parcel source)
            {
                FictionalCharacter info = new FictionalCharacter();
                info.setName(source.readString());
                info.setDescription(source.readString());
                info.setIconUrl(source.readString());
                info.setPageUrl(source.readString());

                return info;
            }

            @Override
            public FictionalCharacter[] newArray(int size)
            {
                return new FictionalCharacter[size];
            }
        };
}