package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import mimishop.yanji.com.mimishop.activity.ShopSearchActivity;
import mimishop.yanji.com.mimishop.activity.mypage.MyJimActivity;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShopDetailActivity;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class MyShopAdapter extends ArrayAdapter<Shop> {

    private Context context;

    private ViewHolder holder;
    private List<Shop> shopList;
    private int resourceId;

    public MyShopAdapter(Context context, int resourceId, List<Shop> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.shopList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private StarRatingBar rating_bar = null;
        private NetworkImageView iv_shop;
        private TextView tv_name;
        private TextView tv_distance;
        private TextView tv_address;
        private TextView tv_time;
        private TextView tv_rest;
        private TextView tv_no;
        private View     ll_left;
        private ImageView iv_seleceted;
    }

    public int getCount() {
        if (shopList != null) {
            return shopList.size();
        }
        return 0;
    }

    public Shop getItem(int position) {
        if (shopList != null) {
            return shopList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (shopList != null) {
            return shopList.get(position).getId();
        }
        return 0;
    }

    public List<Shop> getItemList() {
        return this.shopList;
    }

    public void setItemList(List<Shop> _itemCardList) {
        this.shopList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Shop shop = shopList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_shop = (NetworkImageView) convertView.findViewById(R.id.iv_shop);
            holder.rating_bar = (StarRatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name) ;
            holder.tv_address= (TextView)convertView.findViewById(R.id.tv_location) ;
            holder.tv_distance = (TextView)convertView.findViewById(R.id.tv_distance) ;
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time) ;
            holder.tv_rest = (TextView)convertView.findViewById(R.id.tv_rest) ;
            holder.tv_no = (TextView)convertView.findViewById(R.id.tv_no);
            holder.ll_left = convertView.findViewById(R.id.ll_left);
            holder.iv_seleceted = (ImageView)convertView.findViewById(R.id.iv_selected_img);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context.getClass() == ShopSearchActivity.class) {
                        ((ShopSearchActivity)context).startShopDetailActivity(shop);
                    }
                    if(context.getClass() == MyJimActivity.class) {
                        ((MyJimActivity)context).startShopDetailActivity(shop);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(context.getClass() == ShopSearchActivity.class) {
            // theme
            if(position == 0) {
                holder.ll_left.setVisibility(View.VISIBLE);
            }
            else {
                holder.ll_left.setVisibility(View.GONE);
            }
        }

        if (shop.getImage() != null) {
            holder.iv_shop.setImageUrl(shop.getImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.iv_shop.setImageUrl(null, AppController.getInstance().getImageLoader());
            holder.iv_shop.setDefaultImageResId(R.drawable.loading_330_280_default);
        }

        holder.tv_name.setText(shop.getName());
        holder.tv_distance.setText(Utility.getInstance().getDistance(shop));
        holder.tv_address.setText(shop.getAddress());
        holder.tv_rest.setText(shop.getRestDay());
        holder.tv_time.setText(shop.getShopOpenTime());
        holder.tv_no.setText(String.format("%d", (position+1)));

        if(holder.rating_bar != null) {
            holder.rating_bar.setRating(shop.getShopRemarkLevel());
        }

        return convertView;
    }

}