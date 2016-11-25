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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.cast.CastDetailActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.modal.CastComment;
import mimishop.yanji.com.mimishop.modal.CastDetail;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.ExtendedViewPager;
import mimishop.yanji.com.mimishop.view.ScaleNetworkImageView;

public class CastDetailViewPagerAdapter extends PagerAdapter {
    public static final String TAG = "CastDetailViewPagerAdapter";

    private Context mContext;
    private int mRsrc;
    private ArrayList<CastDetail> mList;
    private int mCount;
    private View mFirstView = null;
    private View mLastView = null;

    private OnClickListener mOnClickListener;

    private class ViewHolder {
        private ScaleNetworkImageView iv_cast;
        private NetworkImageView  iv_main_cast;
        private TextView         tv_explain;
        private TextView         tv_publish;
        private ImageButton      ib_back;
    }

    public CastDetailViewPagerAdapter(Context context, int rsrc, ArrayList<CastDetail> list) {
        mContext = context;
        mRsrc = rsrc;
        mList = list;
        mCount = list.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if(object != null) {
            ViewHolder holder = (ViewHolder) ((View) object).getTag();
            ScaleNetworkImageView imageView = holder.iv_cast;

            ((ExtendedViewPager) container).mCurrentView = imageView;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        final CastDetail store = mList.get(position);

        View convertView = null;

        int resid = mRsrc;
        if(position == 0) {
            resid = R.layout.view_cast_detail_start;
        }
        else  if(position == (mList.size() - 1)) {
            resid = R.layout.view_cast_detail_end;
        }

        convertView = LayoutInflater.from(mContext).inflate(resid, null);
        ViewHolder holder = new ViewHolder();
        if(position == 0) {
            initStartPage(convertView);
            holder.iv_cast = (ScaleNetworkImageView)convertView.findViewById(R.id.iv_cast_main);
            mFirstView = convertView;
        }
        else if(position == (mList.size() - 1)) {
            initEndPage(convertView);
            mLastView = convertView;
        }
        else {
            holder.iv_cast = (ScaleNetworkImageView) convertView.findViewById(R.id.iv_cast);
            holder.tv_explain = (TextView) convertView.findViewById(R.id.tv_content);
            holder.iv_main_cast = (NetworkImageView)convertView.findViewById(R.id.iv_cast_main);
            holder.ib_back = (ImageButton) convertView.findViewById(R.id.ib_back);
            holder.tv_publish = (TextView) convertView.findViewById(R.id.tv_publish);
            initMain(holder, store);
        }
        convertView.setTag(holder);
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

    private  void initStartPage(final View view) {

        final Cast cast = CastDetailActivity.getInstance().getCast();

        ImageButton ibBack = (ImageButton)view.findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().finishActivity();
            }
        });

        CircledNetworkImageView imageView = (CircledNetworkImageView)view.findViewById(R.id.iv_profile);
        TextView tvName = (TextView)view.findViewById(R.id.tv_profile_name);

        if(cast.getCastPostManID() != null) {
            ((TextView)view.findViewById(R.id.tv_output)).setText("있음");
            if(cast.getCastPostManImgURL() != null) {
                imageView.setImageUrl(cast.getCastPostManImgURL(), AppController.getInstance().getImageLoader());
            }
            else {
                imageView.setDefaultImageResId(R.drawable.bg_profile_default_img);
            }
            tvName.setText(cast.getCastPostManID());
        }
        else  {
            ((TextView)view.findViewById(R.id.tv_output)).setText("없음");
            imageView.setDefaultImageResId(R.drawable.bg_profile_default_img);
            tvName.setText("미미샵");
        }

        ScaleNetworkImageView ivCastMain = (ScaleNetworkImageView)view.findViewById(R.id.iv_cast_main);
        if(cast.getCoverImage() != null) {
            ivCastMain.setImageUrl(cast.getCoverImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }

        ivCastMain.setDefaultImageResId(R.drawable.loading_720_1230_default);


        ((TextView)view.findViewById(R.id.tv_title)).setText(cast.getTitle());
        ((TextView)view.findViewById(R.id.tv_like_count)).setText(String.format("%d", cast.getCastHeartCnt()));
        ((TextView)view.findViewById(R.id.tv_comment_count)).setText(String.format("%d", cast.getCastCommentCnt()));
        ((TextView)view.findViewById(R.id.tv_person_count)).setText(String.format("%d", cast.getCastShareCnt()));

        TextView tvTime = (TextView)view.findViewById(R.id.tv_time);

        // modify 10/01
        tvTime.setText(cast.getCastPostTimeString());

        View ll_heart = view.findViewById(R.id.ll_heart);
        ll_heart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().likeCast();
            }
        });

        ll_heart = view.findViewById(R.id.ll_comment);
        ll_heart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().startWriteCastCommentDialog();
            }
        });

        ll_heart = view.findViewById(R.id.ll_share);
        ll_heart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility utility = Utility.getInstance();
                utility.doShareText((Activity) mContext);
                CastDetailActivity.getInstance().shareCast();
            }
        });
    }

    private void initMain(ViewHolder holder, CastDetail store) {

        if (store.getCoverImage() != null) {
            holder.iv_cast.setImageUrl(store.getCoverImage().getImgUrl(), AppController.getInstance().getImageLoader());
        }
        holder.iv_cast.setDefaultImageResId(R.drawable.loading_720_1230_default);

        if(false) {
            Cast cast = CastDetailActivity.getInstance().getCast();
            if (cast.getCoverImage() != null) {
                holder.iv_main_cast.setImageUrl(cast.getCoverImage().getImgUrl(), AppController.getInstance().getImageLoader());
            } else {
                holder.iv_main_cast.setDefaultImageResId(R.drawable.btn_transparent_normal);
            }
        }

        holder.tv_explain.setText(store.getTitle());
        //holder.tv_publish.setText("출처: " + store.getPublish());
        holder.tv_publish.setText(store.getPublish());

        holder.ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().gotoFirstPage();
            }
        });
    }

    private void initEndPage(final View view) {
        final Cast cast = CastDetailActivity.getInstance().getCast();

        ImageButton ibBack = (ImageButton)view.findViewById(R.id.ib_back);
        ibBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().finishActivity();
            }
        });

        ((TextView)view.findViewById(R.id.tv_title)).setText(cast.getTitle());

        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTimeInMillis(currentTime);
        ((TextView)view.findViewById(R.id.tv_date)).setText(formatter.format(calendar.getTime()));

        CircledNetworkImageView imageView = (CircledNetworkImageView)view.findViewById(R.id.iv_profile);
        TextView tvName = (TextView)view.findViewById(R.id.tv_profile_name);

        if(cast.getCastPostManID() != null) {
            if(cast.getCastPostManImgURL() != null) {
                imageView.setImageUrl(cast.getCastPostManImgURL(), AppController.getInstance().getImageLoader());
            }
            else {
                imageView.setDefaultImageResId(R.drawable.bg_profile_default_img);
            }
            tvName.setText(cast.getCastPostManID());
        }
        else  {
            imageView.setDefaultImageResId(R.drawable.bg_profile_default_img);
            tvName.setText("미미샵");
        }

        ((TextView)view.findViewById(R.id.tv_like_count)).setText(String.format("%d", cast.getCastHeartCnt()));
        ((TextView)view.findViewById(R.id.tv_share_count)).setText(String.format("%d", cast.getCastShareCnt()));
        ((TextView)view.findViewById(R.id.tv_comment_count)).setText(String.format("%d", cast.getCastCommentCnt()));

        ImageButton ibLike = (ImageButton)view.findViewById(R.id.ib_like);
        ibLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                CastDetailActivity.getInstance().likeCast();
            }
        });

        ImageButton ibShare = (ImageButton)view.findViewById(R.id.ib_share);
        ibShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
            Utility utility = Utility.getInstance();
            utility.doShareText((Activity) mContext);
            CastDetailActivity.getInstance().shareCast();
            }
        });

        m_ivEmptyImage = (ImageView)view.findViewById(R.id.iv_cast_comment_empty);
        m_lvBestCastComment = (ListView)view.findViewById(R.id.lv_best_cast_comment);
        initBestCommentList();

        m_lvBestCastComment.setSelector(R.drawable.btn_transparent_normal);
        m_lvBestCastComment.setItemsCanFocus(false);

        View rlComment = view.findViewById(R.id.rl_comment);
        rlComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().startWriteCastCommentDialog();
            }
        });
        View rlBottom = view.findViewById(R.id.rl_bottom);
        rlBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CastDetailActivity.getInstance().startWriteCastCommentDialog();
            }
        });
    }

    private void initBestCommentList() {
        Cast cast = CastDetailActivity.getInstance().getCast();

        m_lvBestCastComment.setAdapter(new ArrayAdapter<CastComment>(mContext, R.layout.item_row_best_cast_comment, m_arrCastComment) {

            class ViewHolder {
                private CircledNetworkImageView civ_user;
                private TextView tv_comment;
                private TextView tv_user_name;
                private TextView tv_time;
                private TextView tv_like_count;
                private Button   btn_like;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final CastComment best = getItem(position);
                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_best_cast_comment, null);

                    holder = new ViewHolder();
                    holder.civ_user = (CircledNetworkImageView) convertView.findViewById(R.id.civ_user);
                    holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                    holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_date);
                    holder.tv_like_count = (TextView) convertView.findViewById(R.id.tv_like_count);
                    holder.btn_like = (Button) convertView.findViewById(R.id.btn_like);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if (best.getUserImage() != null) {
                    holder.civ_user.setImageUrl(best.getUserImage().getImgUrl(), AppController.getInstance().getImageLoader());
                } else {
                    holder.civ_user.setDefaultImageResId(R.drawable.bg_profile_default_img);
                }

                holder.tv_user_name.setText(best.getName());
                holder.tv_comment.setText(best.getContent());
                holder.tv_time.setText(Utility.getInstance().getTime1(best.getTime()));
                holder.tv_like_count.setText(String.format("%d", best.getCastcommentHeartCnt()));

                holder.btn_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int likeCnt = best.getCastcommentHeartCnt();
                        likeCnt++;
                        best.setCastcommentHeartCnt(likeCnt);
                        requestLikeCastComment(best);
                    }
                });

                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CastDetailActivity.getInstance().startWriteCastCommentDialog();
                    }
                });

                return convertView;
            }
        });

        requestGetCastCommentList(1, cast.getId());
    }

    private void  showBestComment() {
        if(m_arrCastComment.size() > 0) {
            ((ArrayAdapter<CastComment>)m_lvBestCastComment.getAdapter()).notifyDataSetChanged();
            m_ivEmptyImage.setVisibility(View.GONE);
            m_lvBestCastComment.setVisibility(View.VISIBLE);
        }
        else {
            m_ivEmptyImage.setVisibility(View.VISIBLE);
            m_lvBestCastComment.setVisibility(View.GONE);
        }
    }

    public void refreshUI(int position) {
        if(position == (getCount() - 1)) {
            initEndPage(mLastView);
        }
        else if(position == 0) {
            initStartPage(mFirstView);
        }
    }

    private ListView m_lvBestCastComment = null;
    private ImageView m_ivEmptyImage = null;
    private ArrayList<CastComment> m_arrCastComment = new ArrayList<CastComment>();

    private void requestGetCastCommentList(int type, int cast_idx) {

        String url = ServerAPIPath.API_GET_BEST_CAST_COMMENT_LIST;

        url += "?type="+type+"&cast_id="+cast_idx;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        responseGetCastCommentList(o);
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void responseGetCastCommentList(Object json) {
        try {
            m_arrCastComment.clear();

            JSONArray dataArray = (JSONArray)json;

            int cnt = dataArray.length();
            for(int i = 0; i < cnt; i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                CastComment advertise = new CastComment(jsonObject);

                if(i >= 2) {
                   break;
                }

                m_arrCastComment.add(advertise);
            }

            showBestComment();

        } catch (Exception e) {
            Toast.makeText(mContext, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "responseGetCastCommentList - JSONException : " + e.getMessage());
        }
    }

    public void requestLikeCastComment(final CastComment comment) {
        String url = ServerAPIPath.API_POST_LIKE_CAST_COMMENT;

        Map<String, String> params = new HashMap<String, String>();

        params.put("cast_comment_id", String.format("%d", comment.getId()));

        ServerAPICall.getInstance().callPOST(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {

                            JSONObject object = (JSONObject)o;
                            int cnt = object.getInt("cnt");
                            boolean duplicate = object.getBoolean("isDuplicate");
                            if(cnt != comment.getCastcommentHeartCnt()) {
                                comment.setCastcommentHeartCnt(cnt);
                            }

                            if(duplicate == true) {
                                Toast.makeText(mContext, ResponseMessage.SUCCESS_DUPLICATE_LIKE, Toast.LENGTH_SHORT).show();
                            }

                            ((ArrayAdapter<CastComment>)m_lvBestCastComment.getAdapter()).notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            Toast.makeText(mContext, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    }
                },
                params);
    }
}