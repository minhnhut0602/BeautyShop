package mimishop.yanji.com.mimishop.view;

/**
 * Created by KCJ on 3/24/2015.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.R;

public class StarRatingBar extends LinearLayout {

    public static final int MAX_RATING = 5;

    private ArrayList<ImageView> _arrButton = new ArrayList<ImageView>();
    private TextView _levelText = null;
    private int _rating = 0;
    private int _maxRating = MAX_RATING;

    public StarRatingBar(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public StarRatingBar(Context context, int rating){
        super(context);

        _rating = rating;
    }

    public StarRatingBar(Context context, AttributeSet attr, int defStyle){
        super(context, attr, defStyle);

        TypedArray a = context.obtainStyledAttributes(attr, R.styleable.rating_bar_star_attributes, defStyle, 0);
        int  rating = a.getInteger(R.styleable.rating_bar_star_attributes_rating, 0);
        _rating = rating;

        int  maxRating = a.getInteger(R.styleable.rating_bar_star_attributes_max_rating, MAX_RATING);
        _maxRating = maxRating;

        this.createChildControls(context);
    }

    public StarRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void createChildControls(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        for(int i = 0; i < _maxRating; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.view_star_selector);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            _arrButton.add(imageView);

            this.addView(imageView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        }

        _levelText = new TextView(context);
        LayoutParams params =  new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        _levelText.setTextSize(12);
        _levelText.setTextColor(getResources().getColor(R.color.text_gray));
        this.addView(_levelText, params);

        refreshUI();
    }

    private void refreshUI() {

        int count = _arrButton.size();
        int rating = _rating;

        if(_rating > count){
            rating = count;
        }

        for (int i = 0; i < count; i++) {
            _arrButton.get(i).setSelected(false);
        }

        for(int i = 0; i < rating; i++) {
            _arrButton.get(i).setSelected(true);
        }

        _levelText.setText(String.format("%d", rating));
    }

    public void setRating(int rating) {

        if(rating < 0) {
            rating = 0;
        }
        else if(rating > _maxRating) {
            rating = _maxRating;
        }
        this._rating = rating;

        refreshUI();
    }

    public int getRating(){
        return _rating;
    }

    public int getMaxRating() {
        return _maxRating;
    }
}
