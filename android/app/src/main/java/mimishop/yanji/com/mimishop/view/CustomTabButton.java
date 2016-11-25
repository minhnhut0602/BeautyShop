package mimishop.yanji.com.mimishop.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import mimishop.yanji.com.mimishop.R;

/**
 * Created by KCJ on 4/24/2015.
 */
public class CustomTabButton  extends RelativeLayout {
    private Button  _button;

    private ImageView _underSelected;
    private String  _text;

    public CustomTabButton(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public CustomTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabButton(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        //then obtain typed array
        int[] textInputTypeAttr = new int[] { android.R.attr.text };
        TypedArray arr = context.obtainStyledAttributes(attr, textInputTypeAttr);
        _text = arr.getString(0);
        arr.recycle();

        this.createChildControls(context);
    }

    private void createChildControls(Context context) {

        _button = new Button(context);

        _button.setBackgroundColor(getResources().getColor(R.color.transparent));
        _button.setText(_text);
        _button.setTextSize(13);
        _button.setTextColor(getResources().getColor(R.color.light_text_gray));

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(_button, params);

        _underSelected = new ImageView(context);
        _underSelected.setImageResource(R.drawable.bg_spinner_line);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, 8);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        _underSelected.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(_underSelected, params);
        _underSelected.setVisibility(View.GONE);
    }

    public void refreshSelect(boolean selected) {
        if(selected == true) {
            _underSelected.setVisibility(View.VISIBLE);
        }
        else {
            _underSelected.setVisibility(View.GONE);
        }
    }

    public void setOnClickEvent(OnClickListener listener) {
        _button.setOnClickListener(listener);
    }
}
