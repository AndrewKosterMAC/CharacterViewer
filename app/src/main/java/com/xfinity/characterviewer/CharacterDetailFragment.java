package com.xfinity.characterviewer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CharacterDetailFragment extends Fragment implements ICharacterDetailFragment
{
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        void onCharacterDetailFragmentInteraction(Uri uri);
    }

    private OnFragmentInteractionListener listener;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.placeholderTextDisplay)
    TextView placeholderTextDisplay;

    FictionalCharacter character = null;

    @Override
    public void setCharacter(FictionalCharacter value)
    {
        character = value;

        if (null != character)
        {
            placeholderTextDisplay.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            if (null == webView.getUrl() || !webView.getUrl().equals(character.getPageUrl()))
            {
                webView.loadUrl(character.getPageUrl());
            }
        }
    }

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_character_detail, container, false);

        ButterKnife.bind(this, view);

        if (null != character)
        {
            webView.loadUrl(character.getPageUrl());
        }

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
        {
            listener = (OnFragmentInteractionListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        listener = null;
    }

    public CharacterDetailFragment()
    {
        // Required empty public constructor
    }
}