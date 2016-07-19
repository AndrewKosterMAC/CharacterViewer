package com.xfinity.characterviewer;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsViewHolder extends RecyclerView.ViewHolder
{
    View view;

    @BindView(R.id.nameDisplay)
    TextView nameDisplay;

    public void setName(String value)
    {
        nameDisplay.setText(value);
    }

    @BindView(R.id.descriptionDisplay)
    TextView descriptionDisplay;

    public void setDescription(String value)
    {
        descriptionDisplay.setText(value);
    }

    @BindView(R.id.imageDisplay)
    ImageView imageDisplay;

    @BindDrawable(R.drawable.silhouette)
    Drawable defaultImage;

    public void setImage(String value)
    {
        if (null == value || value.length() < 1)
        {
            imageDisplay.setImageDrawable(defaultImage);
        }
        else
        {
            Glide.with(view.getContext()).load(Uri.parse(value)).into(imageDisplay);
        }
    }

    public ItemsViewHolder(View itemView)
    {
        super(itemView);

        view = itemView;

        view.setTag(this);

        ButterKnife.bind(this, view);
    }
}