package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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

public class ShopImageAdapter extends ArrayAdapter<Image> {

    private Context context;

    private ViewHolder holder;
    private List<Image> shopList;
    private int resourceId;
    private int selectedItemPostion;

    public ShopImageAdapter(Context context, int resourceId, List<Image> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.shopList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private NetworkImageView iv_shop;
        private ImageView iv_selected;
        private ImageView iv_temp;
        private ImageButton ib_delete;
        private View      ic_empty;
        private View      rl_main;
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
            holder.ib_delete = (ImageButton) convertView.findViewById(R.id.ib_delete);
            holder.iv_temp = (ImageView) convertView.findViewById(R.id.iv_temp);
            holder.ic_empty = convertView.findViewById(R.id.ic_empty);
            holder.rl_main = convertView.findViewById(R.id.rl_main);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (shop.getImgUrl() != null || shop.getBitmap() != null) {

            if(shop.getBitmap() != null) {
                holder.iv_temp.setImageBitmap(shop.getBitmap());
                holder.iv_temp.setVisibility(View.VISIBLE);
                holder.iv_shop.setVisibility(View.GONE);
            }
            else if(shop.getImgUrl() != null){
                holder.iv_temp.setVisibility(View.GONE);
                holder.iv_shop.setVisibility(View.VISIBLE);
                holder.iv_shop.setImageUrl(shop.getImgUrl(), AppController.getInstance().getImageLoader());
            }

            holder.ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shop.setDeleted(true);
                    shopList.remove(shop);

                    Image newImage = new Image();
                    newImage.setJustRemove(true);

                    if(false) {
                        shopList.set(position, newImage);
                    }

                    shopList.add(newImage);
                    ShopImageAdapter.this.notifyDataSetChanged();
                }
            });

            holder.rl_main.setVisibility(View.VISIBLE);
            holder.ic_empty.setVisibility(View.GONE);
        }
        else {
            holder.rl_main.setVisibility(View.GONE);
            holder.ic_empty.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setSelectedItemPostion(int pos) {
        selectedItemPostion = pos;
    }

}