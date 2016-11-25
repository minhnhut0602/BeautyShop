package mimishop.yanji.com.mimishop.activity.shopinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.adapter.CastDetailViewPagerAdapter;
import mimishop.yanji.com.mimishop.adapter.ImageViewPagerAdapter;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.modal.CastDetail;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.view.ExtendedViewPager;

public class ImageViewActivity extends Activity {

    private ExtendedViewPager mViewPager;
    private ImageViewPagerAdapter mCastDetailViewPagerAdapter;
    private ArrayList<Image> mImageList = new ArrayList<Image>();

    private int selectedIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        if(Common.mTempList == null) {
            finish();
            return;
        }

        mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);

        mImageList = Common.mTempList;

        selectedIdx = getIntent().getIntExtra("idx", 0);

        mCastDetailViewPagerAdapter = new ImageViewPagerAdapter(this, R.layout.item_row_image_view, mImageList);
        mViewPager.setAdapter(mCastDetailViewPagerAdapter);
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mViewPager.setCurrentItem(selectedIdx, true);

        ImageButton btnBack = (ImageButton)findViewById(R.id.ib_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
