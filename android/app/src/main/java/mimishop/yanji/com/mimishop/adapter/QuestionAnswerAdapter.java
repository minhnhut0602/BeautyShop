package mimishop.yanji.com.mimishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.mypage.MyShopManageActivity;
import mimishop.yanji.com.mimishop.activity.shopinfo.ShowQuestionAndAnswer;
import mimishop.yanji.com.mimishop.modal.Answer;
import mimishop.yanji.com.mimishop.modal.Question;
import mimishop.yanji.com.mimishop.modal.User;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 3/26/2015.
 */
public class QuestionAnswerAdapter extends BaseExpandableListAdapter {
    private ArrayList<Question> groupList = null;
    private ArrayList<ArrayList<Answer>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private boolean isEnableWriteAnswer = false;

    private int group_item_layout_id;
    private int child_item_layout_id;
    private Context mContext;

    public QuestionAnswerAdapter(Context c, ArrayList<Question> groupList,
                                 ArrayList<ArrayList<Answer>> childList, int group_id, int child_id){
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
        mContext = c;

        group_item_layout_id = group_id;
        child_item_layout_id = child_id;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public Question getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(group_item_layout_id, parent, false);
            viewHolder.tv_content = (TextView) v.findViewById(R.id.tv_talk_content);
            viewHolder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            viewHolder.tv_date = (TextView) v.findViewById(R.id.tv_time);
            viewHolder.tv_remove_comment = (TextView)v.findViewById(R.id.tv_remove_comment);
            viewHolder.tv_modify_comment = (TextView)v.findViewById(R.id.tv_modify_comment);

            viewHolder.layout_buttons = v.findViewById(R.id.ll_buttons);
            viewHolder.btn_write_comment = (Button)v.findViewById(R.id.btn_write_answer);

            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        final Question qeustion = getGroup(groupPosition);

        viewHolder.tv_name.setText(qeustion.getName());
        viewHolder.tv_date.setText(Utility.getInstance().getTime(qeustion.getTime()));
        viewHolder.tv_content.setText(qeustion.getContent());

        if(viewHolder.btn_write_comment != null) {
            if(isEnableWriteAnswer == false) {
                viewHolder.btn_write_comment.setVisibility(View.GONE);
                viewHolder.layout_buttons.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.btn_write_comment.setVisibility(View.VISIBLE);
                viewHolder.layout_buttons.setVisibility(View.GONE);

                viewHolder.btn_write_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mContext.getClass() == MyShopManageActivity.class) {
                            ((MyShopManageActivity)mContext).createComment(qeustion);
                        }
                    }
                });
            }
        }

        if(viewHolder.tv_modify_comment != null) {
            viewHolder.tv_modify_comment.setVisibility(View.VISIBLE);
            viewHolder.tv_modify_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext.getClass() == ShowQuestionAndAnswer.class) {
                        ((ShowQuestionAndAnswer)mContext).modifyQuestion(qeustion);
                    }
                }
            });
        }

        if(viewHolder.tv_remove_comment != null) {
            viewHolder.tv_remove_comment.setVisibility(View.VISIBLE);
            viewHolder.tv_remove_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext.getClass() == ShowQuestionAndAnswer.class) {
                        ((ShowQuestionAndAnswer)mContext).removeQuestion(qeustion);
                    }
                }
            });
        }

        User user = AppController.getInstance().getCurrentUser();

        if(user.getId() != qeustion.getShopcommentPostManID()) {
            viewHolder.layout_buttons.setVisibility(View.GONE);
        }

        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public Answer getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(child_item_layout_id, null);
            viewHolder.tv_content = (TextView) v.findViewById(R.id.tv_talk_content);
            viewHolder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            viewHolder.tv_date = (TextView) v.findViewById(R.id.tv_time);
            viewHolder.layout_buttons = v.findViewById(R.id.ll_buttons);
            viewHolder.btn_modify_comment = (Button)v.findViewById(R.id.btn_modify_comment);
            viewHolder.btn_remove_comment= (Button)v.findViewById(R.id.btn_remove_comment);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        final Answer answer = getChild(groupPosition, childPosition);
        final Question qeustion = getGroup(groupPosition);
        viewHolder.tv_name.setText(answer.getName());
        viewHolder.tv_date.setText(Utility.getInstance().getTime(answer.getTime()));
        viewHolder.tv_content.setText(answer.getContent());

        if(isEnableWriteAnswer == true) {
            Button  button = viewHolder.btn_modify_comment;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext.getClass() == MyShopManageActivity.class) {
                        ((MyShopManageActivity)mContext).modifyAnswer(qeustion, answer);
                    }
                }
            });

            Button  button1 = viewHolder.btn_remove_comment;
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext.getClass() == MyShopManageActivity.class) {
                        ((MyShopManageActivity)mContext).removeAnswer(answer);
                    }
                }
            });
            viewHolder.layout_buttons.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.layout_buttons.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public boolean hasStableIds() { return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class ViewHolder {
        public TextView tv_name;
        public TextView tv_date;
        public TextView tv_content;
        public TextView tv_remove_comment;
        public TextView tv_modify_comment;
        public Button btn_write_comment;
        public Button btn_remove_comment;
        public Button btn_modify_comment;
        public View layout_buttons;
    }

    public  void setEnableWriteAnswer() {
       isEnableWriteAnswer = true;
    }
}


