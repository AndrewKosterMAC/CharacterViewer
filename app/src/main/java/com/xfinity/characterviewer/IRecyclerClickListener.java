package com.xfinity.characterviewer;

import android.view.View;

public interface IRecyclerClickListener
{
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}