package mimishop.yanji.com.mimishop.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.App;
import mimishop.yanji.com.mimishop.modal.Category;

/**
 * Created by John on 1/20/2016.
 */
public class AgreeFragmentViewPagerAdapter extends PagerAdapter {

    public static final String TAG = "AgreeFragmentViewPagerAdapter";
    private Context mContext;
    private int mRsrc;
    private List<String> mList;
    private int type = 0;
    public AgreeFragmentViewPagerAdapter(Context context, int rsrc, List<String> list,int type) {
        mContext = context;
        mRsrc = rsrc;
        mList = list;
        this.type = type;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View convertView = null;
        convertView = LayoutInflater.from(mContext).inflate(mRsrc, null);
        TextView wv_privacy = (TextView)convertView.findViewById(R.id.wv_privacy);
/*
        WebView m_wvUserUse = (WebView)convertView.findViewById(R.id.wv_privacy);
        m_wvUserUse.getSettings().setJavaScriptEnabled(true);
//        m_wvUserUse.getSettings().setDefaultTextEncodingName("UTF-8");
        m_wvUserUse.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        m_wvUserUse.setWebChromeClient(new WebChromeClient());
        m_wvUserUse.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

            }
        });
*/
//        App app = AppController.getInstance().getApp();
//        m_wvUserUse.loadUrl(app.getAppUserUseURL());
//        Log.e("test",app.getAppUserUseURL());
//        if(type == 0)
//            m_wvUserUse.loadData(mList.get(position),"text/html","UTF-8");
//        else m_wvUserUse.loadData(mList.get(2), "text/html", "UTF-8");

          if(type == 0)
              wv_privacy.setText(mList.get(position));
           else wv_privacy.setText(mList.get(2));

        ((ViewPager) container).addView(convertView, 0);
        return convertView;
    }

    @Override
    public int getCount() {
        if(type == 0) return 2;
        else return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(type == 0) {
            if(position == 0) return  mContext.getResources().getString(R.string.agree_title_1);
            else if(position == 1) return  mContext.getResources().getString(R.string.agree_title_2);
        }
        return "";
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {

        return view == o;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager) pager).removeView((View) view);
    }
    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }


}
