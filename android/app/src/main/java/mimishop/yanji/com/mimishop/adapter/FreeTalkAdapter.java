package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.util.List;

import mimishop.yanji.com.mimishop.activity.MainActivity;
import mimishop.yanji.com.mimishop.fragment.FreeTalkFragment;
import mimishop.yanji.com.mimishop.modal.FreeTalk;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.EllipsisTextView;
import mimishop.yanji.com.mimishop.view.ImageArrayLayout;

public class FreeTalkAdapter extends ArrayAdapter<FreeTalk> {

    private Context context;

    private List<FreeTalk> freetalkList;
    private int resourceId;

    public FreeTalkAdapter(Context context, int resourceId, List<FreeTalk> freetalkList) {
        super(context, resourceId, freetalkList);

        this.context = context;
        this.freetalkList = freetalkList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private CircledNetworkImageView iv_level;
        private ImageArrayLayout iv_image;
        private TextView tv_nickname;
        private TextView tv_time;
        private EllipsisTextView tv_comment;
        private TextView tv_category;
        private TextView tv_likecount;
        private TextView tv_commentcount;
        private TextView tv_visitorcount;
        private TextView tv_level;
        private Button   btn_give_heart;
        private Button   btn_write_comment;
        private Button   btn_share;
        private TextView tv_see_more;
    }

    public int getCount() {
        if (freetalkList != null) {
            return freetalkList.size();
        }
        return 0;
    }

    public FreeTalk getItem(int position) {
        if (freetalkList != null) {
            return freetalkList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (freetalkList != null) {
            return freetalkList.get(position).getId();
        }
        return 0;
    }

    public List<FreeTalk> getItemList() {
        return this.freetalkList;
    }

    public void setItemList(List<FreeTalk> _itemCardList) {
        this.freetalkList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final FreeTalk freeTalk = freetalkList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_level = (CircledNetworkImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_login_time);
            holder.tv_comment = (EllipsisTextView) convertView.findViewById(R.id.tv_talk_content);
            holder.tv_likecount = (TextView) convertView.findViewById(R.id.tv_like_count);
            holder.tv_visitorcount = (TextView) convertView.findViewById(R.id.tv_declare_count);
            holder.tv_commentcount = (TextView) convertView.findViewById(R.id.tv_comment_count);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.btn_give_heart = (Button) convertView.findViewById(R.id.btn_give_heart);
            holder.btn_write_comment = (Button) convertView.findViewById(R.id.btn_write_comment);
            holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
            holder.iv_image = (ImageArrayLayout) convertView.findViewById(R.id.iv_image_array);
            holder.tv_see_more = (TextView) convertView.findViewById(R.id.tv_see_more);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (freeTalk.getLevelImageURL() != null) {
            holder.iv_level.setImageUrl(freeTalk.getLevelImageURL(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.iv_level.setImageUrl(null, AppController.getInstance().getImageLoader());
            holder.iv_level.setDefaultImageResId(R.drawable.bg_person_default);
        }


        if (freeTalk.getFreetalkImgArray() != null) {
            holder.iv_image.setVisibility(View.VISIBLE);
            holder.iv_image.setImageArray(freeTalk.getFreetalkImgArray());
        }
        else {
            holder.iv_image.setVisibility(View.GONE);
        }

        holder.tv_level.setText(String.format("%d",freeTalk.getLevel()));

        holder.tv_nickname.setText(freeTalk.getUserNickName());
        holder.tv_time.setText(Utility.getInstance().getTime1(freeTalk.getCrtPostTime()));
        holder.tv_comment.setText(freeTalk.getContent());

        if(freeTalk.getCategory() != null) {
            holder.tv_category.setText(freeTalk.getCategory().getName());
        }

        holder.tv_likecount.setText(String.format("%d",freeTalk.getLikeCount()));
        holder.tv_commentcount.setText(String.format("%d",freeTalk.getCommentCount()));
        holder.tv_visitorcount.setText(String.format("%d",freeTalk.getDeclareCount()));

        holder.btn_give_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) context;
                FreeTalkFragment frag = (FreeTalkFragment) activity.getVisibleFragment();

                frag.doLikeFreeTalk(freeTalk);
            }
        });

        holder.btn_write_comment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)context;
                FreeTalkFragment frag = (FreeTalkFragment)activity.getVisibleFragment();

                User user = AppController.getInstance().getCurrentUser();
                if(user.isLimitUser() == true) {
                    AppController.showLimitUserDlg(activity);
                    return;
                }
                frag.startWritingCommentActivity(freeTalk, false);
            }
        });

        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)context;
                FreeTalkFragment frag = (FreeTalkFragment)activity.getVisibleFragment();

                frag.doShare(freeTalk);
            }
        });

        holder.tv_comment.setMaxLines(7);
        final ViewHolder placeHolder = holder;
        holder.tv_comment.addEllipsesListener(new EllipsisTextView.EllipsisListener() {
            @Override
            public void ellipsisStateChanged(boolean ellipses) {
                if(ellipses == true) {
                    placeHolder.tv_see_more.setVisibility(View.VISIBLE);
                }
                else {
                    placeHolder.tv_see_more.setVisibility(View.GONE);
                }
            }
        });

        holder.tv_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)context;
                FreeTalkFragment frag = (FreeTalkFragment)activity.getVisibleFragment();

                frag.startWritingCommentActivity(freeTalk, true);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity)context;
                FreeTalkFragment frag = (FreeTalkFragment)activity.getVisibleFragment();

                frag.startWritingCommentActivity(freeTalk, true);
            }
        });

        return convertView;
    }

}