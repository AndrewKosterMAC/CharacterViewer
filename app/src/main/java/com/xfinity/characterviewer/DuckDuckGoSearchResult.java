package com.xfinity.characterviewer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DuckDuckGoSearchResult implements Parcelable
{
    public String getHeading()
    {
        return heading;
    }

    @SerializedName("Heading")
    private String heading;

    public void setHeading(String value)
    {
        heading = value;
    }

    public List<Map<String, Object>> getRelatedTopics()
    {
        return relatedTopics;
    }

    @SerializedName("RelatedTopics")
    private List<Map<String, Object>> relatedTopics;

    public void setRelatedTopics(List<Map<String, Object>> value)
    {
        relatedTopics = value;
    }

    public List<FictionalCharacter> getCharacters()
    {
        return characters;
    }

    private List<FictionalCharacter> characters = new ArrayList<>();

    public void setCharacters(List<FictionalCharacter> value)
    {
        characters = value;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i)
    {
        dest.writeString(getHeading());
        dest.writeTypedList(getCharacters());
    }

    public static final Parcelable.Creator<DuckDuckGoSearchResult> CREATOR =
        new Parcelable.Creator<DuckDuckGoSearchResult>()
        {
            @Override
            public DuckDuckGoSearchResult createFromParcel(Parcel source)
            {
                DuckDuckGoSearchResult info = new DuckDuckGoSearchResult();
                info.setHeading(source.readString());
                info.setCharacters(source.readArrayList(FictionalCharacter.class.getClassLoader()));

                return info;
            }

            @Override
            public DuckDuckGoSearchResult[] newArray(int size)
            {
                return new DuckDuckGoSearchResult[size];
            }
        };
}