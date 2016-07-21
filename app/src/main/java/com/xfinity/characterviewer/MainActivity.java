package com.xfinity.characterviewer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements CharactersListFragment.OnFragmentInteractionListener,
        CharacterDetailFragment.OnFragmentInteractionListener,
        NetworkStateReceiver.NetworkStateReceiverListener
{
    @BindView(R.id.networkStatusDisplay)
    TextView networkStatusDisplay;

    @BindView(R.id.searchField)
    EditText searchField;

    @BindView(R.id.charactersListView)
    RecyclerView charactersListView;

    RecyclerView.Adapter charactersListViewAdapter;

    CharacterDataService characterDataService = null;

    private boolean characterDataServiceIsBound = false;

    private NetworkStateReceiver networkStateReceiver = null;

    private ServiceConnection characterDataServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            CharacterDataService.DataServiceBinder binder = (CharacterDataService.DataServiceBinder)iBinder;
            characterDataService = binder.getService();

            characterDataServiceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            characterDataServiceIsBound = false;
        }
    };

    private boolean isNetworkAvailable()
    {
        return isNetworkAvailable(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo());
    }

    private boolean isNetworkAvailable(NetworkInfo networkInfo)
    {
        return networkInfo != null && networkInfo.isAvailable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.main);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        charactersListView.setLayoutManager(new LinearLayoutManager(this));

        charactersListView.setItemAnimator(new DefaultItemAnimator());

        charactersListView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        charactersListViewAdapter = new RecyclerView.Adapter<ItemsViewHolder>()
        {
            @Override
            public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View itemView
                    = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.character_list_item, parent, false);

                return new ItemsViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(ItemsViewHolder holder, int position)
            {
                if (null != characterDataService)
                {
                    FictionalCharacter character = characterDataService.getDisplayedCharacters().get(position);

                    holder.setName(character.getName());
                    holder.setDescription(character.getDescription());
                    holder.setImage(character.getIconUrl());
                }
            }

            @Override
            public int getItemCount()
            {
                if (null == characterDataService)
                {
                    return 0;
                }

                return characterDataService.getDisplayedCharacters().size();
            }
        };
        charactersListView.setAdapter(charactersListViewAdapter);

        charactersListView.addOnItemTouchListener(new RecyclerTouchListener(this, charactersListView, new IRecyclerClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
                if (null != characterDataService)
                {
                    FictionalCharacter character = characterDataService.getDisplayedCharacters().get(position);

                    if (getResources().getBoolean(R.bool.isTablet))
                    {
                        ICharacterDetailFragment characterDetailFragment = (ICharacterDetailFragment)getSupportFragmentManager().findFragmentById(R.id.characterDetailFragment);

                        characterDetailFragment.setCharacter(character);
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, CharacterDetailActivity.class);
                        intent.putExtra("character", (Parcelable)character);

                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position)
            {
            }
        }));

        if (isNetworkAvailable())
        {
            startDataService();
        }
        else
        {
            networkStatusDisplay.setVisibility(View.VISIBLE);
            networkStatusDisplay.setText(getResources().getText(R.string.network_not_available));

            networkStateReceiver = new NetworkStateReceiver();
            networkStateReceiver.addListener(this);
            this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void startDataService()
    {
        Handler characterDataServiceMessageHandler = new Handler()
        {
            @Override
            public void handleMessage(Message message)
            {

                if (null != characterDataService)
                {
                    characterDataService.filterDisplayedCharacters(searchField.getText().toString());
                    charactersListViewAdapter.notifyDataSetChanged();
                }
            }
        };

        Intent intent = new Intent(this, CharacterDataService.class);
        intent.putExtra("dataMessenger", new Messenger(characterDataServiceMessageHandler));
        intent.putExtra("isNetworkAvailable", isNetworkAvailable());

        startService(intent);
        bindService(intent, characterDataServiceConnection, Context.BIND_AUTO_CREATE);

        searchField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (null != characterDataService)
                {
                    characterDataService.filterDisplayedCharacters(searchField.getText().toString());
                    charactersListViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (characterDataServiceIsBound)
        {
            unbindService(characterDataServiceConnection);
            characterDataServiceIsBound = false;
        }
    }

    @Override
    public void onCharactersListFragmentInteraction(Uri uri)
    {
    }

    @Override
    public void onCharacterDetailFragmentInteraction(Uri uri)
    {
    }

    @Override
    public void networkAvailable()
    {
        networkStatusDisplay.setVisibility(View.GONE);
        networkStatusDisplay.setText("");

        startDataService();
    }

    @Override
    public void networkUnavailable()
    {
    }
}