package mimishop.yanji.com.mimishop.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.CastDetail;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.view.ExtendedViewPager;
import mimishop.yanji.com.mimishop.view.ScaleNetworkImageView;

public class ImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int mRsrc;
    private ArrayList<Image> mList;
    private int mCount;

    private OnClickListener mOnClickListener;

    private class ViewHolder {
        private ScaleNetworkImageView iv_cast;
        private TextView         tv_explain;
    }

    public ImageViewPagerAdapter(Context context, int rsrc, ArrayList<Image> list) {
        mContext = context;
        mRsrc = rsrc;
        mList = list;
        mCount = list.size();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((ExtendedViewPager)container).mCurrentView = (ScaleNetworkImageView)object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        final Image store = mList.get(position);

        View convertView = null;
        ViewHolder holder = null;

        convertView = LayoutInflater.from(mContext).inflate(mRsrc, null);
        holder = new ViewHolder();
        holder.iv_cast = (ScaleNetworkImageView) convertView.findViewById(R.id.iv_cast);
        holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_content);

        convertView.setTag(holder);

        if (store.getImgUrl() != null) {
            holder.iv_cast.setImageUrl(store.getImgUrl(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.iv_cast.setDefaultImageResId(R.drawable.loading_720_1230_default);
        }

        ((ViewPager) container).addView(convertView, 0);

        convertView.setOnClickListener(mOnClickListener);

        return convertView;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager) pager).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

}