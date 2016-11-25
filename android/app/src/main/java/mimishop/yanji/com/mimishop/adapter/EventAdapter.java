package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.util.Utility;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;

    private ViewHolder holder;
    private List<Event> eventList;
    private int resourceId;

    public EventAdapter(Context context, int resourceId, List<Event> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private NetworkImageView iv_event;
        private NetworkImageView iv_banner;
        private NetworkImageView iv_location;
        private TextView         tv_location;
        private TextView         tv_summary;
        private TextView         tv_explain;
        private TextView         tv_shop_name;
        private TextView         tv_money;
        private TextView         tv_click_cnt;
        private View             rl_click_cnt;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Event getItem(int position) {
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

    public List<Event> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Event> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Event event = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_event = (NetworkImageView) convertView.findViewById(R.id.iv_event);
            holder.iv_banner = (NetworkImageView) convertView.findViewById(R.id.iv_banner);
            holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_click_cnt = (TextView) convertView.findViewById(R.id.tv_click_cnt);
            holder.rl_click_cnt = convertView.findViewById(R.id.rl_click_cnt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl_click_cnt.setVisibility(View.GONE);
        holder.iv_banner.setVisibility(View.GONE);
        if (event.getEventImage() != null) {
            holder.iv_event.setImageUrl(event.getEventImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.iv_event.setImageUrl(null, AppController.getInstance().getImageLoader());
            holder.iv_event.setDefaultImageResId(R.drawable.loading_290_260_default);
        }


        if (holder.tv_shop_name != null) {
            holder.tv_shop_name.setText(event.getShopName());
        }

        if(holder.tv_location != null) {
            holder.tv_location.setText(event.getSubLocation());
        }

        if(holder.tv_summary != null) {
            holder.tv_summary.setText(event.getEventSummary());
        }

        if(holder.tv_money != null) {
            holder.tv_money.setText(String.format("%d 원", event.getPrice()));
        }

        if(holder.tv_explain != null) {
            holder.tv_explain.setText(event.getEventExplain());
        }

        if(holder.tv_click_cnt  != null) {
                holder.tv_click_cnt.setText(String.format("%d", event.getEventClickCnt()));
        }

        return convertView;
    }

}