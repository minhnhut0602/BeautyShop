package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.util.Utility;

public class ImageAdapter extends ArrayAdapter<Image> {

    private Context context;

    private ViewHolder holder;
    private List<Image> shopList;
    private int resourceId;
    private AdapterView<ListAdapter> listView;
    private int selectedItemPostion = -1;

    public ImageAdapter(Context context, int resourceId, List<Image> shopList, AdapterView<ListAdapter> listView) {
        super(context, resourceId, shopList);

        this.context = context;
        this.shopList = shopList;
        this.resourceId = resourceId;
        this.listView = listView;
    }

    private class ViewHolder {
        private NetworkImageView iv_shop;
        private ImageView iv_selected;
        private TextView tv_name;
        private TextView tv_distance;
        private TextView tv_address;
        private TextView tv_time;
        private TextView tv_rest;
    }

    public int getCount() {
        if (shopList != null) {
            return shopList.size();
        }
        return 0;
    }

    public Image getItem(int position) {
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

    public List<Image> getItemList() {
        return this.shopList;
    }

    public void setItemList(List<Image> _itemCardList) {
        this.shopList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Image shop = shopList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_shop = (NetworkImageView) convertView.findViewById(R.id.iv_shop);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name) ;
            holder.tv_address= (TextView)convertView.findViewById(R.id.tv_location) ;
            holder.tv_distance = (TextView)convertView.findViewById(R.id.tv_distance) ;
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time) ;
            holder.tv_rest = (TextView)convertView.findViewById(R.id.tv_rest) ;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (shop.getImgUrl() != null) {
            holder.iv_shop.setImageUrl(shop.getImgUrl(), AppController.getInstance().getImageLoader());
        }

        if(selectedItemPostion == position) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_selected.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setSelectedItemPostion(int pos) {
        selectedItemPostion = pos;
    }
    public int getSelectedItemPostion() {
        return selectedItemPostion;
    }

}