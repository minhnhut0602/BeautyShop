package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import mimishop.yanji.com.mimishop.activity.freetalk.WriteCommentActivity;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.Event;
import mimishop.yanji.com.mimishop.modal.Shop;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.CircledNetworkImageView;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private Context context;

    private ViewHolder holder;
    private List<Comment> eventList;
    private int resourceId;

    public CommentAdapter(Context context, int resourceId, List<Comment> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private CircledNetworkImageView  civ_photo;
        private TextView         tv_user_name;
        private TextView         tv_time;
        private TextView         tv_content;
        private Button           btn_like;
        private TextView         tv_heart_cnt;
        private FrameLayout      fl_layout;
        private FrameLayout      fl_last;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Comment getItem(int position) {
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

    public List<Comment> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Comment> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Comment comment = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.civ_photo = (CircledNetworkImageView) convertView.findViewById(R.id.civ_user);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.btn_like = (Button) convertView.findViewById(R.id.btn_like);
            holder.tv_heart_cnt = (TextView) convertView.findViewById(R.id.tv_like_count);
            holder.fl_layout = (FrameLayout)convertView.findViewById(R.id.fl_first);
            holder.fl_last = (FrameLayout)convertView.findViewById(R.id.fl_last);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(getCount() == 1) {
            holder.fl_layout.setVisibility(View.VISIBLE);
            holder.fl_last.setVisibility(View.VISIBLE);
        }
        else {
            if (position == 0) {
                holder.fl_layout.setVisibility(View.VISIBLE);
                holder.fl_last.setVisibility(View.GONE);
            } else if (position == (getCount() - 1)) {
                holder.fl_last.setVisibility(View.VISIBLE);
                holder.fl_layout.setVisibility(View.GONE);
            }
            else {
                holder.fl_layout.setVisibility(View.GONE);
                holder.fl_last.setVisibility(View.GONE);
            }
        }
        if(comment.getUserImageURL() != null) {
            holder.civ_photo.setImageUrl(comment.getUserImageURL(), AppController.getInstance().getImageLoader());
        }
        else {
            holder.civ_photo.setImageUrl(null, AppController.getInstance().getImageLoader());
            holder.civ_photo.setDefaultImageResId(R.drawable.bg_person_default);
        }

        holder.tv_user_name.setText(comment.getName());
        if(comment.getRedLists().size() > 0) {
            ArrayList<Comment.RedList> list = comment.getRedLists();
            Spannable wordtoSpan = new SpannableString(comment.getContent());
            for(int i = 0; i < list.size(); i++) {
                Comment.RedList red = list.get(i);
                wordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_background_color)),
                        red.start, red.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.tv_content.setText(wordtoSpan);
        }
        else {
            holder.tv_content.setText(comment.getContent());
        }

        holder.tv_time.setText(Utility.getInstance().getTime1(comment.getTime()));
        holder.tv_heart_cnt.setText(String.format("%d", comment.getCommentHeartCnt()));

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(context.getClass() == WriteCommentActivity.class) {
                    WriteCommentActivity activity = (WriteCommentActivity)context;
                    activity.requestLikeFreetalkComment(comment);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context.getClass() == WriteCommentActivity.class) {
                    User user  = AppController.getInstance().getCurrentUser();
                    WriteCommentActivity activity = (WriteCommentActivity)context;

                    if(user.getId() == comment.getUserID()) {
                        activity.showRemoveDialog(comment);
                    }
                    else {
                        activity.showDeclareAndTagDialog(comment);
                    }
                }
            }
        });

        return convertView;
    }

}