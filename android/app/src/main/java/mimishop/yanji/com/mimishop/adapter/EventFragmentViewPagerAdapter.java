package mimishop.yanji.com.mimishop.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.event.EventLifeActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.fragment.EventFragment;
import mimishop.yanji.com.mimishop.modal.Advertise;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.Life;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class EventFragmentViewPagerAdapter extends PagerAdapter {

    public static final String TAG = "EventFragmentViewPagerAdapter";
    private Context mContext;
    private int mRsrc;
    private ArrayList<Category> mList;

    private OnClickListener mOnClickListener;

    private class ViewHolder {
        private ListView listview;
        private int idx;
        private int preLast = 0;
        private int prePageNum = -1;
        private int pageItemCnt = 10;
        private ArrayList<Life> arrLife = new ArrayList<Life>();
        private NetworkImageView banner;
    }

    public EventFragmentViewPagerAdapter(Context context, int rsrc, ArrayList<Category> list) {
        mContext = context;
        mRsrc = rsrc;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        final Category store = mList.get(position);

        View convertView = null;
        final ViewHolder holder = new ViewHolder();

        convertView = LayoutInflater.from(mContext).inflate(mRsrc, null);
        holder.listview = (ListView)convertView.findViewById(R.id.lv_events);
        holder.idx = position;
        convertView.setTag(holder);

        holder.listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastItem = firstVisibleItem + visibleItemCount;

                    if (lastItem == totalItemCount) {
                        if (holder.preLast != lastItem) {
                            holder.preLast = lastItem;
                            addItems(totalItemCount);
                        }
                    }
            }

            private void addItems(int totalItemCount) {
                int pageNum = (totalItemCount / holder.pageItemCnt) + 1;
                requestEventList(holder, pageNum, false);
            }
        });

        ((ViewPager) container).addView(convertView, 0);

        requestEventList(holder, 1, true);

        convertView.setOnClickListener(mOnClickListener);

        return convertView;
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
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager) pager).removeView((View) view);
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

    private void requestEventList(final ViewHolder holder, int pageNum, boolean isChangeParam) {
        if (holder.prePageNum == pageNum && isChangeParam == false) {
            return;
        }

        holder.prePageNum = pageNum;

        String url = ServerAPIPath.API_GET_LIFE_EVNET_LIST;

        boolean isChange = false;
        if(holder.idx != 0) {
            //url += "?category_id=" + EventFragment.getInstance().getCategoryIDX(holder.idx);
            url += "?category_id=" + ((EventLifeActivity)mContext).getCategoryIDX(holder.idx);
            isChange = true;
        }

        if(pageNum >= 1) {
            if(isChange == true) {
                url += "&page_num=" + pageNum;
            }
            else {
                url += "?page_num=" + pageNum;
            }
        }
        Utility.showWaitingDlg((Activity)mContext);
        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseEventList(o, holder);
                        Utility.hideWaitingDlg();
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                        Utility.hideWaitingDlg();
                    }
                });
    }

    private void responseEventList(Object json, ViewHolder holder){

        try {
            JSONObject jsonObject = (JSONObject)json;
            int total = jsonObject.getInt("total");
            int page_num = jsonObject.getInt("page");
            holder.pageItemCnt = jsonObject.getInt("rows_per_page");
            JSONArray dataArray = jsonObject.getJSONArray("list");

            ArrayList<Life> arrEventList = new ArrayList<Life>();
            ArrayAdapter<Life> prevAdapter = (ArrayAdapter<Life>)holder.listview.getAdapter();
            if(prevAdapter != null) {
                arrEventList = holder.arrLife;
            }

            for(int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                Life event = new Life(dataObject);

                arrEventList.add(event);
            }

            if(prevAdapter != null) {
                prevAdapter.notifyDataSetChanged();
            }
            else {
                final  ArrayList<Life> lifeList = arrEventList;
                holder.listview.setAdapter(new ArrayAdapter<Life>(mContext, R.layout.item_row_life, lifeList) {

                    class LifeViewHolder {
                        private NetworkImageView iv_event;
                        private NetworkImageView iv_banner;
                        private NetworkImageView iv_location;
                        private TextView tv_location;
                        private TextView tv_summary;
                        private TextView tv_title;
                        private TextView tv_explain;
                        private TextView tv_shop_name;
                        private TextView tv_money;
                        private TextView tv_click_cnt;
                        private View ll_title;
                        private View ll_explain;
                        private View ll_image;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final Life event = lifeList.get(position);

                        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                        LifeViewHolder lifeViewHolder = null;
                        if (convertView == null) {
                            convertView = mInflater.inflate(R.layout.item_row_life, null);
                            lifeViewHolder = new LifeViewHolder();
                            lifeViewHolder.iv_event = (NetworkImageView) convertView.findViewById(R.id.iv_event);
                            lifeViewHolder.iv_banner = (NetworkImageView) convertView.findViewById(R.id.iv_banner);
                            lifeViewHolder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
                            lifeViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                            lifeViewHolder.ll_title = convertView.findViewById(R.id.ll_title);
                            lifeViewHolder.ll_image = convertView.findViewById(R.id.rl_image);
                            lifeViewHolder.ll_explain = convertView.findViewById(R.id.ll_explain);
                            lifeViewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
                            lifeViewHolder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
                            lifeViewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
                            lifeViewHolder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
                            lifeViewHolder.tv_click_cnt = (TextView) convertView.findViewById(R.id.tv_click_cnt);
                            convertView.setTag(lifeViewHolder);
                        } else {
                            lifeViewHolder = (LifeViewHolder) convertView.getTag();
                        }

                        if (event.getLifeURL() != null) { // life event
                            lifeViewHolder.iv_banner.setImageUrl(event.getLifeImgURL(), AppController.getInstance().getImageLoader());
                            lifeViewHolder.iv_banner.setDefaultImageResId(R.drawable.loading_990_360);
                            lifeViewHolder.iv_banner.setVisibility(View.VISIBLE);

                            if (event.getSubject().isEmpty() == true) {
                                if (event.getExplain().isEmpty() == true) {
                                    lifeViewHolder.ll_explain.setVisibility(View.GONE);
                                } else {
                                    lifeViewHolder.ll_title.setVisibility(View.GONE);
                                }
                            } else {
                                lifeViewHolder.ll_title.setVisibility(View.VISIBLE);
                                lifeViewHolder.ll_explain.setVisibility(View.VISIBLE);
                            }

                            lifeViewHolder.tv_title.setText(event.getSubject());
                            lifeViewHolder.tv_explain.setText(event.getExplain());

                            final  View imageView = lifeViewHolder.ll_image;
                            ViewTreeObserver vto = imageView.getViewTreeObserver();
                            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                    int width = imageView.getMeasuredWidth();
                                    int height = imageView.getMeasuredHeight();
                                    double ratio = Common.LIFE_BANNER_RATIO;
                                    width = View.MeasureSpec.getSize(width);
                                    height = (int) (width / ratio);

                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                                    params.height = height;
                                    imageView.setLayoutParams(params);
                                }
                            });
                        }

                        return convertView;
                    }
                });
            }

            final ArrayList<Life> arrayList = arrEventList;
            holder.arrLife = arrayList;
            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //EventFragment.getInstance().gotoEventDetail(arrayList.get(i));
                    ((EventLifeActivity)mContext).gotoEventDetail(arrayList.get(i));
                }
            });
        } catch (Exception e) {
            Toast.makeText(mContext, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseEventList - JSONException : " + e.getMessage());
        }
    }


}