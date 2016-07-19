package com.xfinity.characterviewer;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 */
public interface IDuckDuckGoCharacterApi
{
    @GET(BuildConfig.DATA_URL_QUERY)
    public Call<DuckDuckGoSearchResult> getCharacters();
}