package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;

public class ShopPremiereAdapter extends ArrayAdapter<Shop> {

    private Context context;

    private ViewHolder holder;
    private List<Shop> shopList;
    private int resourceId;

    public ShopPremiereAdapter(Context context, int resourceId, List<Shop> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.shopList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private NetworkImageView iv_shop;
        private ImageView iv_shop_local;
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
            holder.iv_shop_local = (ImageView) convertView.findViewById(R.id.iv_shop_local);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (shop.getImage() != null) {
            holder.iv_shop.setImageUrl(shop.getImage().getImgUrl(), AppController.getInstance().getImageLoader());
            holder.iv_shop_local.setVisibility(View.GONE);
        }
        else {
            holder.iv_shop_local.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

}