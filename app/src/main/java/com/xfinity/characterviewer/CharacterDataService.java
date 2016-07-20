package com.xfinity.characterviewer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Gets character data from a remote service and stores it as an in-memory data structure.
 */
public class CharacterDataService extends Service
{
    /**
     * Allows other components to bind to the service.
     */
    public class DataServiceBinder extends Binder
    {
        CharacterDataService getService()
        {
            return CharacterDataService.this;
        }
    }

    private IBinder binder = new DataServiceBinder();

    private List<FictionalCharacter> allCharacters = new ArrayList<>();

    public List<FictionalCharacter> getDisplayedCharacters()
    {
        synchronized (characterListsLock)
        {
            return new ArrayList<>(displayedCharacters);
        }
    }

    private List<FictionalCharacter> displayedCharacters = new ArrayList<>();

    private final Object characterListsLock = new Object();

    public void filterDisplayedCharacters(String searchTerm)
    {
        synchronized (characterListsLock)
        {
            displayedCharacters.clear();

            searchTerm = searchTerm.toLowerCase();

            for (FictionalCharacter character : allCharacters)
            {
                if (null == searchTerm
                        || searchTerm.length() < 1
                        || character.getName().toLowerCase().contains(searchTerm)
                        || character.getDescription().toLowerCase().contains(searchTerm)) {
                    displayedCharacters.add(character);
                }
            }
        }
    }

    /**
     * Binds to another component via an Intent.
     *
     * @param intent the calling Intent.
     * @return the Binder.
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        getData(intent);

        return binder;
    }

    private void getData(Intent intent)
    {
        Messenger dataMessenger = (Messenger)intent.getExtras().get("dataMessenger");
        boolean isNetworkAvailable = intent.getBooleanExtra("isNetworkAvailable", false);

        if (isNetworkAvailable)
        {
            getDataFromInternet(dataMessenger);
        }
        else
        {
            getDataFromCache(dataMessenger);
        }
    }

    private void getDataFromCache(Messenger dataMessenger)
    {
    }

    private void getDataFromInternet(final Messenger dataMessenger)
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.duckduckgo.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                IDuckDuckGoCharacterApi service = retrofit.create(IDuckDuckGoCharacterApi.class);

                Call<DuckDuckGoSearchResult> call = service.getCharacters();

                call.enqueue(new Callback<DuckDuckGoSearchResult>()
                {
                    @Override
                    public void onResponse(Call<DuckDuckGoSearchResult> call, Response<DuckDuckGoSearchResult> response)
                    {
                        DuckDuckGoSearchResult duckDuckGoSearchResult = response.body();

                        synchronized (characterListsLock)
                        {
                            for (Map<String, Object> characterData : duckDuckGoSearchResult.getRelatedTopics())
                            {
                                FictionalCharacter character = new FictionalCharacter();

                                String[] characterTextParts = characterData.get("Text").toString().split(" - ");

                                character.setName(characterTextParts[0]);
                                character.setDescription(characterTextParts[1]);

                                Map icon = (Map) characterData.get("Icon");
                                String iconUrl = (String) icon.get("URL");

                                character.setIconUrl(iconUrl);

                                character.setPageUrl("https://duckduckgo.com/html/?q=" + character.getName());

                                allCharacters.add(character);
                                //duckDuckGoSearchResult.getCharacters().add(character);
                            }
                        }

                        Message message = Message.obtain();
                        //message.obj = duckDuckGoSearchResult;

                        try
                        {
                            dataMessenger.send(message);
                        }
                        catch (RemoteException ex)
                        {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    public void onFailure(Call<DuckDuckGoSearchResult> call, Throwable t)
                    {

                    }
                });
            }
        };
        thread.start();
    }

    /**
     * Constructor.
     */
    public CharacterDataService()
    {
    }
}