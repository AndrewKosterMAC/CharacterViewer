package com.xfinity.characterviewer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CharacterDetailActivity extends AppCompatActivity
    implements CharacterDetailFragment.OnFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_phone_character_detail);

        FictionalCharacter character = (FictionalCharacter)getIntent().getSerializableExtra("character");

        getSupportActionBar().setTitle(character.getName());

        ICharacterDetailFragment characterDetailFragment = (ICharacterDetailFragment)getSupportFragmentManager().findFragmentById(R.id.characterDetailFragment);

        characterDetailFragment.setCharacter(character);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onCharacterDetailFragmentInteraction(Uri uri)
    {

    }
}