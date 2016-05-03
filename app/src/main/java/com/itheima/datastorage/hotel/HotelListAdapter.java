package com.itheima.datastorage.hotel;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.datastorage.R;
import com.itheima.datastorage.model.Hotel;
import com.itheima.datastorage.ui.BaseViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**酒店列表适配器
 * Created by youliang.ji on 2016/5/1.
 */
public class HotelListAdapter extends ArrayAdapter<Hotel>{

    private DisplayImageOptions options;

    public HotelListAdapter(Context context,  List<Hotel> data) {
        super(context, 0, data);

        setImageLoaderOptions();
    }

    private void setImageLoaderOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_default)
                .showImageForEmptyUri(R.drawable.image_default)
                .showImageOnFail(R.drawable.image_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())//default
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.getViewHolder(getContext(), convertView, parent, R.layout.hotel_list_item_layout, position);

        Hotel hotel = getItem(position);
        ImageView mImageView = holder.findViewById(R.id.image);
        TextView mTvName = holder.findViewById(R.id.tv_name);
        TextView mTvPrice = holder.findViewById(R.id.tv_price);
        ImageLoader.getInstance().displayImage(hotel.getImg(), mImageView);
        mTvName.setText(hotel.getName());
        mTvPrice.setText("价格："+hotel.getPrice());

        return holder.getConvertView();
    }
}
