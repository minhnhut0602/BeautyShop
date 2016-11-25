package mimishop.yanji.com.mimishop.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.shopinfo.ImageViewActivity;
import mimishop.yanji.com.mimishop.constant.Common;
import mimishop.yanji.com.mimishop.modal.Comment;
import mimishop.yanji.com.mimishop.modal.Image;
import mimishop.yanji.com.mimishop.util.LruBitmapCache;
import mimishop.yanji.com.mimishop.util.SelectPhotoManager;

/**
 * Created by KimCholJu on 5/13/2015.
 */
public class ImageArrayLayout extends FrameLayout implements View.OnClickListener, View.OnTouchListener  {

    private static final int MAX_VIEW_CNT = 4;

    private ArrayList<Image> _images = null;

    private View m_vgContainer = null;

    private NetworkImageView []m_arrNetImgView = new NetworkImageView[MAX_VIEW_CNT];

    private View m_llMain1 = null;
    private View m_llMain2 = null;

    private int     m_nViewMode = 0;

    public ImageArrayLayout(Context context) {
        super(context);
        this.createChildControls(context);
    }

    public ImageArrayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.createChildControls(context);
    }

    /* (non-Javadoc)
	 * @see android.view.View#onFinishInflate()
	 */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initChildControls(getContext());
    }

    private void createChildControls(Context context) {

    }

    private void initChildControls(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_vgContainer = (ViewGroup) inflater.inflate(R.layout.view_image_array_layout, null);
        if (m_vgContainer == null)
            return;

        addView(m_vgContainer);

        m_arrNetImgView[0] = (NetworkImageView)m_vgContainer.findViewById(R.id.iv_image_1);
        m_arrNetImgView[1] = (NetworkImageView)m_vgContainer.findViewById(R.id.iv_image_2);
        m_arrNetImgView[2] = (NetworkImageView)m_vgContainer.findViewById(R.id.iv_image_3);
        m_arrNetImgView[3] = (NetworkImageView)m_vgContainer.findViewById(R.id.iv_image_4);

        for(int i = 0; i < MAX_VIEW_CNT; i++) {
            final int idx = i;
            m_arrNetImgView[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.mTempList = _images;
                    Intent intent = new Intent(getContext(), ImageViewActivity.class);

                    intent.putExtra("idx", idx);
                    getContext().startActivity(intent);
                }
            });
        }

        m_llMain1 = m_vgContainer.findViewById(R.id.ll_main_1);
        m_llMain2 = m_vgContainer.findViewById(R.id.ll_main_2);

        refresh();
    }


    private void refresh() {

        if(m_vgContainer == null || _images == null) {
            return;
        }

        int size = _images.size();

        this.setVisibility(VISIBLE);
        m_llMain1.setVisibility(VISIBLE);
        m_llMain2.setVisibility(VISIBLE);

        for(int i = 0; i < MAX_VIEW_CNT; i++) {
            m_arrNetImgView[i].setVisibility(VISIBLE);

            NetworkImageView imageView =  m_arrNetImgView[i];
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            imageView.setLayoutParams(params);

            if(i == 0 && (size == 1 || size == 3)) {
                m_arrNetImgView[0].setDefaultImageResId(R.drawable.loading_654_330);
            }
            else {
                m_arrNetImgView[i].setDefaultImageResId(R.drawable.loading_330_330);
            }


            if(i < size) {
                m_arrNetImgView[i].setImageUrl(_images.get(i).getImgUrl(), AppController.getInstance().getImageLoader());
            }
        }

        if(size == 0) {
            this.setVisibility(GONE);

            m_nViewMode = 0;
        }
        else if(size == 1) {
            m_llMain2.setVisibility(GONE);
            m_arrNetImgView[1].setVisibility(GONE);
            m_arrNetImgView[0].setDefaultImageResId(R.drawable.loading_654_330);

            m_nViewMode = 1;

            // The pre-existing content of this view didn't match the current URL. Load the new image
            // from the network.
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            String imagUrl = _images.get(0).getImgUrl();
            ImageLoader.ImageContainer newContainer = imageLoader.get(imagUrl, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
                    // If this was an immediate response that was delivered inside of a layout
                    // pass do not set the image immediately as it will trigger a requestLayout
                    // inside of a layout. Instead, defer setting the image by posting back to
                    // the main thread.
                    if (isImmediate ) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse(response, false);
                            }
                        });
                        return;
                    }

                    if (response.getBitmap() != null) {
                        Bitmap bitmap = response.getBitmap();
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        if(bitmap != null) {

                            NetworkImageView imageView = m_arrNetImgView[0];
                            int finalHeight = imageView.getMeasuredHeight();
                            int finalWidth = imageView.getMeasuredWidth();

                            int bmpWidth = bitmap.getWidth();
                            int bmpHeight = bitmap.getHeight();
                            float scaleRatio = (float) bmpHeight / bmpWidth;

                            finalHeight = (int) (scaleRatio * finalWidth);

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                            params.width = finalWidth;
                            params.height = finalHeight;
                            imageView.setLayoutParams(params);
                        }
                    }
                }
            });
        }
        else if(size == 2) {
            m_llMain2.setVisibility(GONE);
            m_nViewMode = 2;

        }
        else if(size == 3) {
            m_arrNetImgView[0].setDefaultImageResId(R.drawable.loading_654_330);
            m_arrNetImgView[1].setVisibility(GONE);
            m_arrNetImgView[2].setImageUrl(_images.get(1).getImgUrl(), AppController.getInstance().getImageLoader());
            m_arrNetImgView[3].setImageUrl(_images.get(2).getImgUrl(), AppController.getInstance().getImageLoader());

            m_nViewMode = 3;
        }
        else {
            m_nViewMode = 4;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);

        if(m_nViewMode == 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int finalWidth = (parentWidth - 4) / 2;
        for (int i = 0; i < MAX_VIEW_CNT; i++) {

            NetworkImageView imageView = m_arrNetImgView[i];
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = finalWidth;
            params.height = finalWidth;
            imageView.setLayoutParams(params);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    public void setImageArray(ArrayList<Image> _images) {
        this._images = _images;

        refresh();
    }
}
