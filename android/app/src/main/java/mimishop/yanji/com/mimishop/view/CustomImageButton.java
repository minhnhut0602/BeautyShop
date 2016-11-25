package mimishop.yanji.com.mimishop.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mimishop.yanji.com.mimishop.R;

/**
 * Created by KCJ on 5/5/2015.
 */
public class CustomImageButton extends LinearLayout {

    private int _space = 5;
    private String _text = "";
    private int _src = 0;
    private int _textColor = Color.WHITE;
    private ColorStateList _textColorList = null;
    private int _textSize = 13;
    private ImageView   _ibImage = null;
    private TextView    _tvTitle = null;

    public CustomImageButton(Context context) {
        super(context);
        this.createChildControls(context);
    }


    public CustomImageButton(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);

        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.custom_imagebutton_attributes, defStyle, 0);
        _space = a.getInteger(R.styleable.custom_imagebutton_attributes_space, 5);

        _text = a.getString(R.styleable.custom_imagebutton_attributes_text);
        _src = a.getResourceId(R.styleable.custom_imagebutton_attributes_src, R.drawable.layout_bg_pink_border_grey);
        _textColor = a.getColor(R.styleable.custom_imagebutton_attributes_textcolor, Color.WHITE);
        _textColorList = a.getColorStateList(R.styleable.custom_imagebutton_attributes_textcolor);
        _textSize = a.getInteger(R.styleable.custom_imagebutton_attributes_titlesize, 13);
        this.createChildControls(context);
    }

    public CustomImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void createChildControls(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(VERTICAL);
        params.gravity = Gravity.CENTER_VERTICAL;

        this.addView(linearLayout, params);

        params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        _ibImage = new ImageView(context);
        _ibImage.setBackgroundColor(getResources().getColor(R.color.transparent));
        _ibImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        _ibImage.setImageResource(_src);
        _ibImage.setAdjustViewBounds(true);
        _ibImage.setFocusable(false);
        _ibImage.setFocusableInTouchMode(false);
        linearLayout.addView(_ibImage, params);

        _tvTitle = new TextView(context);
        _tvTitle.setGravity(Gravity.CENTER);
        _tvTitle.setFocusable(false);
        _tvTitle.setFocusableInTouchMode(false);
        params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, _space, 0, 0);
        _tvTitle.setText(_text);

        if(_textColorList != null) {
            _tvTitle.setTextColor(_textColorList);
        }
        else {
            _tvTitle.setText(_textColor);
        }

        _tvTitle.setTextSize(_textSize);
        linearLayout.addView(_tvTitle, params);
    }
}
