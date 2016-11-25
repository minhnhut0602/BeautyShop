package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context context;

    private ViewHolder holder;
    private List<Category> shopList;
    private int resourceId;

    public CategoryAdapter(Context context, int resourceId, List<Category> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.shopList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
    }

    public int getCount() {
        if (shopList != null) {
            return shopList.size();
        }
        return 0;
    }

    public Category getItem(int position) {
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

    public List<Category> getItemList() {
        return this.shopList;
    }

    public void setItemList(List<Category> _itemCardList) {
        this.shopList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Category shop = shopList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(shop.getName());

        if(shop.getName().equals("네일")) {
            holder.iv_icon.setImageResource(R.drawable.ic_nail);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("헤어")){
            holder.iv_icon.setImageResource(R.drawable.ic_hair);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("속눈썹연장")){
            holder.iv_icon.setImageResource(R.drawable.ic_soknun);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("왁싱")){
            holder.iv_icon.setImageResource(R.drawable.ic_waksing);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("피부")){
            holder.iv_icon.setImageResource(R.drawable.ic_skin);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("마사지")) {
            holder.iv_icon.setImageResource(R.drawable.ic_massage);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 스피너 클릭시 보여지는 View의 정의
     */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        final Category shop = shopList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(shop.getName());
        holder.tv_name.setTextColor(context.getResources().getColorStateList(R.color.text_cast_item_selector));

        if(shop.getName().equals("네일")) {
            holder.iv_icon.setImageResource(R.drawable.ic_nail2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("헤어")){
            holder.iv_icon.setImageResource(R.drawable.ic_hair2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("속눈썹연장")){
            holder.iv_icon.setImageResource(R.drawable.ic_soknun2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("왁싱")){
            holder.iv_icon.setImageResource(R.drawable.ic_waksing2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("피부")){
            holder.iv_icon.setImageResource(R.drawable.ic_skin2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else if(shop.getName().equals("마사지")) {
            holder.iv_icon.setImageResource(R.drawable.ic_massage2_selector);
            holder.iv_icon.setVisibility(View.VISIBLE);
        }
        else {
            holder.iv_icon.setVisibility(View.GONE);
        }
        convertView.setBackground(context.getResources().getDrawable(R.drawable.bg_list_category_item_selector));

        return convertView;
    }
}