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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.Banner;
import mimishop.yanji.com.mimishop.modal.Cast;

public class CastPremiumViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private int mRsrc;
    private ArrayList<Banner> mList;
    private int mCount;

    private OnClickListener mOnClickListener;

    public CastPremiumViewPagerAdapter(Context context, int rsrc, ArrayList<Banner> list) {
        mContext = context;
        mRsrc = rsrc;
        mList = list;
        mCount = list.size();
    }

    public class ViewHolder {
        private NetworkImageView iv_cast;
        private TextView         tv_explain;
        public  Banner   banner;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        final Banner store = mList.get(position);

        View convertView = null;
        ViewHolder holder = null;

        convertView = LayoutInflater.from(mContext).inflate(mRsrc, null);
        holder = new ViewHolder();
        holder.iv_cast = (NetworkImageView) convertView.findViewById(R.id.iv_cast);
        holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_content);

        convertView.setTag(holder);

        Advertise ads = store.getBannerAds();
        if (ads.getAdsImgURL() != null) {
            holder.iv_cast.setImageUrl(ads.getAdsImgURL(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.iv_cast.setDefaultImageResId(R.drawable.btn_transparent_normal);
        }

        convertView.setOnClickListener(mOnClickListener);
        holder.banner = store;

        ((ViewPager) container).addView(convertView, 0);

        final View rlCastBanner = convertView;
        ViewTreeObserver vto = rlCastBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlCastBanner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = rlCastBanner.getMeasuredWidth();
                int height = rlCastBanner.getMeasuredHeight();

                height = (int)(width/ Common.CAST_BANNER_RATIO);

                rlCastBanner.getLayoutParams().height = height;
            }
        });

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