package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.mypage.MyShopManageActivity;
import mimishop.yanji.com.mimishop.modal.Answer;
import mimishop.yanji.com.mimishop.modal.Product;
import mimishop.yanji.com.mimishop.modal.Question;

/**
 * Created by KimCholJu on 5/22/2015.
 */
public class WriteShopAnswerDialog extends Dialog {

    private Context mContext;
    private  boolean mCreate;
    private Question mQuestion;
    private Answer   mAnswer;

    public WriteShopAnswerDialog(Context context, boolean isCreate, Question question, Answer answer) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        mCreate = isCreate;
        mQuestion = question;
        mAnswer = answer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.dlg_write_answer);

        ImageButton btnExit = (ImageButton)findViewById(R.id.ib_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView tvTitle = (TextView)findViewById(R.id.tv_dlg_title);
        if(mCreate == true) {
            tvTitle.setText("댓글 달기");
        }
        else {
            tvTitle.setText("댓글 변경");
        }

        EditText editText = (EditText)findViewById(R.id.et_comment);
        if(mAnswer != null) {
            editText.setText(mAnswer.getContent());
        }

        Button btnOK = (Button) findViewById(R.id.btn_answer);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.et_comment);

                if(mContext.getClass() == MyShopManageActivity.class) {
                    if(mCreate == true) {
                        ((MyShopManageActivity)mContext).requestWriteAnswer(mQuestion, null, editText.getText().toString());
                    }
                    else {
                        ((MyShopManageActivity)mContext).requestWriteAnswer(mQuestion, mAnswer, editText.getText().toString());
                    }
                }
                dismiss();
            }
        });

        if(true) {
            initKeyboardTouch();
        }
    }

    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    private  void initKeyboardTouch() {

        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }
}
