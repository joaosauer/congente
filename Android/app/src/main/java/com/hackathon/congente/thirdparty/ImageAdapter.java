package com.hackathon.congente.thirdparty;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hackathon.congente.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.resolva_ideia1,
            R.drawable.resolva_ideia2,
            R.drawable.resolva_ideia3,
            R.drawable.resolva_ideia4,
            R.drawable.resolva_ideia5
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //Use this code if you want to load from resources
        ImageView i = new ImageView(mContext);
        i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new CoverFlow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        //Make sure we set anti-aliasing otherwise we get jaggies
        BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
        drawable.setAntiAlias(true);
        return i;
    }
}