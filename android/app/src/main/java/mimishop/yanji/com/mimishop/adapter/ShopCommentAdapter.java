package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mimishop.yanji.com.mimishop.activity.shopinfo.ShowUserAfterActivity;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.util.Utility;
import mimishop.yanji.com.mimishop.view.StarRatingBar;

public class ShopCommentAdapter extends ArrayAdapter<Question> {

    private Context context;

    private ViewHolder holder;
    private List<Question> eventList;
    private int resourceId;

    public ShopCommentAdapter(Context context, int resourceId, List<Question> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        private StarRatingBar    bar_rating;
        private TextView         tv_user_name;
        private TextView         tv_time;
        private TextView         tv_content;
        private TextView         tv_inform;
        private TextView         tv_remove;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Question getItem(int position) {
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

    public List<Question> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Question> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Question event = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.bar_rating = (StarRatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tv_inform = (TextView) convertView.findViewById(R.id.tv_inform);
            holder.tv_remove = (TextView) convertView.findViewById(R.id.tv_remove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder.bar_rating != null) {
            holder.bar_rating.setRating(event.getShopcommentShopLevel());
        }

        holder.tv_user_name.setText("("+event.getName()+")");
        holder.tv_content.setText(event.getContent());

        if(Utility.getInstance().getTime(event.getTime()) != null) {
            holder.tv_time.setText(Utility.getInstance().getTime(event.getTime()));
        }

        boolean mine = false;
        if(event.getShopcommentPostManID() == AppController.getInstance().getCurrentUser().getId())
        {
            mine = true;
        }

        if(holder.tv_inform != null) {
            holder.tv_inform.setPaintFlags(holder.tv_inform.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            if(mine == true) {
                holder.tv_inform.setEnabled(false);
                holder.tv_inform.setTextColor(context.getResources().getColor(R.color.light_text_gray));
            }
            else {
                holder.tv_inform.setEnabled(true);
                holder.tv_inform.setTextColor(context.getResources().getColor(R.color.main_background_color));
            }


            holder.tv_inform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    ShowUserAfterActivity.getInstance().requestionInformUR(event);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("정말 신고하시겠어요?").setPositiveButton("확인", dialogClickListener)
                            .setNegativeButton("취소", dialogClickListener).show();
                }
            });
        }

        if(holder.tv_remove != null) {

            if(mine == false) {
                holder.tv_remove.setEnabled(false);
                holder.tv_remove.setTextColor(context.getResources().getColor(R.color.light_text_gray));
            }
            else {
                holder.tv_remove.setEnabled(true);
                holder.tv_remove.setTextColor(context.getResources().getColor(R.color.main_background_color));
            }

            holder.tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    ShowUserAfterActivity.getInstance().requestRemoveUserRemark(event);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("정말 삭제하시겠어요?").setPositiveButton("확인", dialogClickListener)
                            .setNegativeButton("취소", dialogClickListener).show();
                }
            });
        }

        return convertView;
    }

}