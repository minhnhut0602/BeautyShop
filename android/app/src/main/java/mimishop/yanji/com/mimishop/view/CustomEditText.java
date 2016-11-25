package mimishop.yanji.com.mimishop.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import mimishop.yanji.com.mimishop.R;

/**
 * Created by KCJ on 4/24/2015.
 */
public class CustomEditText  extends RelativeLayout {
    private EditText _edittext;

    private ImageView _bgNormal;
    private ImageView _bgSelected;

    private String _hint="";
    private int    _textSize = 0;
    private int _textInputType = InputType.TYPE_CLASS_TEXT;

    public CustomEditText(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.custom_edittext_attributes, defStyle, 0);
        String  hint = a.getString(R.styleable.custom_edittext_attributes_hint);
        _hint = hint;

        _textSize = a.getInteger(R.styleable.custom_edittext_attributes_textsize, 13);

        //then obtain typed array
        int[] textInputTypeAttr = new int[] { android.R.attr.inputType };
        TypedArray arr = context.obtainStyledAttributes(attr, textInputTypeAttr);
        _textInputType = arr.getInt(0, InputType.TYPE_CLASS_TEXT);
        arr.recycle();

        this.createChildControls(context);
    }

    private void createChildControls(Context context) {

        _edittext = new EditText(context);

        _edittext.setHint(_hint);
        _edittext.setHintTextColor(context.getResources().getColor(R.color.button_gray));

        _edittext.setTextColor(context.getResources().getColor(R.color.text_gray));
        _edittext.setTextSize(_textSize);
        _edittext.setPadding(5,5,5,5);
        _edittext.setLines(2);
        _edittext.setSingleLine(false);

        _edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == true) {
                    _bgSelected.setVisibility(View.VISIBLE);
                    _bgNormal.setVisibility(View.GONE);
                }
                else {
                    _bgSelected.setVisibility(View.GONE);
                    _bgNormal.setVisibility(View.VISIBLE);
                }
            }
        });

        _edittext.setBackgroundResource(R.drawable.btn_transparent_normal);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.addView(_edittext, params);

        _bgNormal = new ImageView(context);
        _bgNormal.setImageResource(R.drawable.bg_underline);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        _bgNormal.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(_bgNormal, params);
        _bgNormal.setVisibility(View.VISIBLE);

        _bgSelected = new ImageView(context);
        _bgSelected.setImageResource(R.drawable.bg_spinner_line);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        _bgSelected.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(_bgSelected, params);
        _bgSelected.setVisibility(View.GONE);

        if(false) {
            _edittext.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            _edittext.setTransformationMethod(new PasswordTransformationMethod());
        }

        _edittext.setInputType(_textInputType);
    }

    public String getText() {
        return _edittext.getText().toString();
    }

    public void setText(String text) {
        _edittext.setText(text);
    }

    public void setOnFocusEvent(OnFocusChangeListener listener) {
        _edittext.setOnFocusChangeListener(listener);
    }

    public void setDisableEdit() {
        _edittext.setFocusable(false);
        _edittext.setFocusableInTouchMode(false);
    }

    public void refreshSelect(boolean selected) {
        if(selected == true) {
            _bgSelected.setVisibility(View.VISIBLE);
            _bgNormal.setVisibility(View.GONE);
        }
        else {
            _bgSelected.setVisibility(View.GONE);
            _bgNormal.setVisibility(View.VISIBLE);
        }
    }
}
