package com.xfinity.characterviewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NetworkStateReceiver extends BroadcastReceiver
{
    public interface NetworkStateReceiverListener
    {
        public void networkAvailable();

        public void networkUnavailable();
    }

    protected List<NetworkStateReceiverListener> listeners;

    protected Boolean connected;

    public void onReceive(Context context, Intent intent)
    {
        if (intent == null || intent.getExtras() == null)
        {
            return;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (null != networkInfo && NetworkInfo.State.CONNECTED == networkInfo.getState())
        {
            connected = true;
        }
        else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE))
        {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll()
    {
        for (NetworkStateReceiverListener listener : listeners)
        {
            notifyState(listener);
        }
    }

    private void notifyState(NetworkStateReceiverListener listener)
    {
        if (connected == null || listener == null)
        {
            return;
        }

        if (connected == true)
        {
            listener.networkAvailable();
        }
        else
        {
            listener.networkUnavailable();
        }
    }

    public void addListener(NetworkStateReceiverListener listener)
    {
        listeners.add(listener);

        notifyState(listener);
    }

    public void removeListener(NetworkStateReceiverListener listener)
    {
        listeners.remove(listener);
    }

    public NetworkStateReceiver()
    {
        listeners = new ArrayList<NetworkStateReceiverListener>();
        connected = null;
    }
}