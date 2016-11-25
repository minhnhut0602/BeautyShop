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

import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;

public class CastAdapter extends ArrayAdapter<Cast> {

    private Context context;

    private ViewHolder holder;
    private List<Cast> eventList;
    private int resourceId;

    public CastAdapter(Context context, int resourceId, List<Cast> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private NetworkImageView iv_cast;
        private TextView         tv_explain;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Cast getItem(int position) {
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

    public List<Cast> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Cast> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Cast event = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_cast = (NetworkImageView) convertView.findViewById(R.id.iv_cast);
            holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (event.getCoverImage() != null) {
            holder.iv_cast.setImageUrl(event.getCoverImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }

        holder.iv_cast.setDefaultImageResId(R.drawable.loading_290_260_default);

        holder.tv_explain.setText(event.getTitle());

        return convertView;
    }

}