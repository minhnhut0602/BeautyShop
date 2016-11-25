package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.LoginActivity;
import mimishop.yanji.com.mimishop.fragment.MoreShopEventFragment;
import mimishop.yanji.com.mimishop.modal.Category;
import mimishop.yanji.com.mimishop.util.Utility;

/**
 * Created by KCJ on 5/5/2015.
 */
public class SelectCategoryDialog extends Dialog implements View.OnClickListener {

    public static final String TAG = "SelectCategoryDialog";

    private int mExtraCount = 2;

    private MoreShopEventFragment mContext;

    private ListView m_lvCategory;


    private ArrayList<Category> m_arrCategory = new ArrayList<Category>();

    private ArrayList<String> m_lstCategory = new ArrayList<String>();

    public SelectCategoryDialog(MoreShopEventFragment context,ArrayList<Category> m_arrCategory) {
        super(context.getActivity(), android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        this.m_arrCategory = m_arrCategory;
        initData();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dlg_select_category);

        Button btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = getViewByPosition(m_lvCategory.getFirstVisiblePosition() + 2, m_lvCategory);
                Rect rect1 = Utility.getFrameForView(view1);
                View view2 = getViewByPosition(m_lvCategory.getFirstVisiblePosition() + 3, m_lvCategory);
                Rect rect2 = Utility.getFrameForView(view2);

                ImageView ivSelected = (ImageView)findViewById(R.id.iv_select);
                Rect rect = Utility.getFrameForView(ivSelected);

                Rect interSect1 = Utility.getIntersectionRect(rect, rect1);
                Rect interSect2 = Utility.getIntersectionRect(rect, rect2);

                int selecteIdx = m_lvCategory.getFirstVisiblePosition();

                if(interSect1 == null) {
                    selecteIdx = selecteIdx +1;
                }
                else if(interSect2 != null){

                    int square1 = interSect1.width()*interSect1.height();
                    int square2 = interSect2.width()*interSect2.height();

                    if(square1 < square2) {
                        selecteIdx = selecteIdx + 1;
                    }
                }

                //String category = m_arrCategory.get(m_lvCategory.getFirstVisiblePosition()).getName();
                if(mContext.getClass() == MoreShopEventFragment.class) {
                    ((MoreShopEventFragment)mContext).categorySelected(selecteIdx);
                }

                dismiss();
            }
        });
        btnOK = (Button) findViewById(R.id.btn_cancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        m_lvCategory = (ListView)findViewById(R.id.lv_category_1);

        m_lvCategory.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_row_location_datapicker, m_lstCategory) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.item_row_location_datapicker, null);
                }

                String location = m_lstCategory.get(position);
                TextView cpText = (TextView) v.findViewById(R.id.tv_location);
                cpText.setText(location);
                return v;
            }
        });

        m_lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View view1 = getViewByPosition(i, m_lvCategory);
                Rect rect1 = Utility.getFrameForView(view1);

                ImageView ivSelected = (ImageView) findViewById(R.id.iv_select);
                Rect rect = Utility.getFrameForView(ivSelected);
                m_lvCategory.smoothScrollBy((rect1.top - rect.top), 100);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_exit:
                onDestroy();
                break;
        }
    }

    private void onDestroy() {
        dismiss();
    }

    private void initData() {
        int size = m_arrCategory.size();

        for(int i = 0; i < mExtraCount; i++) {
            m_lstCategory.add("");
        }
        for(int i = 0; i < size; i++) {
            m_lstCategory.add(m_arrCategory.get(i).getName());
        }
        for(int i = 0; i < mExtraCount; i++) {
            m_lstCategory.add("");
        }
    }
}
