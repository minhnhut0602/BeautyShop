package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class SearchPriceAdapter extends ArrayAdapter<Product> {

    private Context context;

    private ViewHolder holder;
    private List<Product> eventList;
    private int resourceId;

    private boolean isShowRating = false;

    public SearchPriceAdapter(Context context, int resourceId, List<Product> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private TextView         tv_shop_name;
        private TextView         tv_product_name;
        private TextView         tv_right_price;
        private TextView         tv_event_price;
        private StarRatingBar    rb_Rating;
        private TextView         tv_shop_address;
        private TextView         tv_review_count;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Product getItem(int position) {
        if (eventList != null) {
            return eventList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (eventList != null) {
            return eventList.get(position).getId();
        }
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    public List<Product> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Product> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Product event = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
             convertView = mInflater.inflate(resourceId, null);

            holder = new ViewHolder();
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_right_price = (TextView) convertView.findViewById(R.id.tv_right_price);
            holder.tv_event_price = (TextView) convertView.findViewById(R.id.tv_event_price);
            holder.rb_Rating = (StarRatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_shop_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_review_count = (TextView) convertView.findViewById(R.id.tv_review_cnt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_shop_name.setText(event.getShopName());
        holder.tv_product_name.setText(event.getName());
        holder.tv_right_price.setText(event.getPrice());
        holder.tv_event_price.setText(event.getEventPrice());
        holder.rb_Rating.setRating(event.getShopLevel());
        holder.tv_shop_address.setText(event.getShopAddress());
        holder.tv_review_count.setText(String.format("평가 %d건",event.getShopReviewCount()));

        return convertView;
    }

    public void setShowRating(boolean isShowRating) {
        this.isShowRating = isShowRating;
    }

}