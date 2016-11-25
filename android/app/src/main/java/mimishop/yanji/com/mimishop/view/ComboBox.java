package mimishop.yanji.com.mimishop.view;

/**
 * Created by KCJ on 3/24/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import mimishop.yanji.com.mimishop.R;

public class ComboBox extends RelativeLayout {

    private Spinner _spinner;

    public ComboBox(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public ComboBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.createChildControls(context);
    }

    private void createChildControls(Context context) {

        _spinner = new Spinner(context);

        _spinner.setBackgroundResource(R.drawable.btn_transparent_normal);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(_spinner, params);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.bg_spinner_line);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(imageView, params);

        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.bg_spinner_triangle);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        this.addView(imageView, params);
    }


    public  void setAdapter(SpinnerAdapter adapter) {
        _spinner.setAdapter(adapter);
    }


    public void setOnItemClickListener(AdapterView.OnItemSelectedListener selectedListener){
        _spinner.setOnItemSelectedListener(selectedListener);
    }

    public SpinnerAdapter getAdapter(){
        return _spinner.getAdapter();
    }

    public String getSelectedItemText() {
        return _spinner.getSelectedItem().toString();
    }

    public int getSelectedIndex() {
        return _spinner.getSelectedItemPosition();
    }
    public void setSelectedIndex(int idx) {
        _spinner.setSelection(idx);
    }
}
