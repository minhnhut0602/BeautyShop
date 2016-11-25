package mimishop.yanji.com.mimishop.activity.cast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
//import com.facebook.android.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mimishop.yanji.com.mimishop.AppController;
import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Cast;
import mimishop.yanji.com.mimishop.network.ServerAPICall;
import mimishop.yanji.com.mimishop.network.ServerAPIPath;
import mimishop.yanji.com.mimishop.network.SessionManager;
import mimishop.yanji.com.mimishop.network.onErrorListener;
import mimishop.yanji.com.mimishop.network.onReponseListener;
import mimishop.yanji.com.mimishop.util.Logger;
import mimishop.yanji.com.mimishop.util.Utility;

public class CastBestActivity extends Activity {

    public static final String TAG = "CastBaseActivity";
    private static final int MAX_BEST_CNT  = 9;

    private ListView m_lvBestCast = null;
    private TextView m_tvTime = null;
    private ArrayList<Cast> m_arrBestCast = new ArrayList<Cast>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_best);

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_cast_best, menu);
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

    private void init() {
        initMenu();

        m_tvTime = (TextView)findViewById(R.id.tv_time);
        m_lvBestCast = (ListView)findViewById(R.id.lv_best_cast);

        m_lvBestCast.setAdapter(new ArrayAdapter<Cast>(this, R.layout.item_row_cast_best_1, m_arrBestCast) {

            class ViewHolder {
                private NetworkImageView iv_cast;
                private TextView tv_title;
                private TextView tv_prize;
                private TextView tv_number;
                private  View    ll_first;
                private  View    ll_general;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Cast best = getItem(position);
                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_row_cast_best_1, null);

                    holder = new ViewHolder();
                    holder.iv_cast = (NetworkImageView) convertView.findViewById(R.id.iv_cast);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    holder.tv_prize = (TextView) convertView.findViewById(R.id.tv_prize);
                    holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                    holder.ll_first = convertView.findViewById(R.id.ll_first);
                    holder.ll_general = convertView.findViewById(R.id.ll_general);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if(position == 0) {
                    holder.ll_first.setVisibility(View.VISIBLE);
                    holder.ll_general.setVisibility(View.GONE);
                }
                else {
                    holder.ll_first.setVisibility(View.GONE);
                    holder.ll_general.setVisibility(View.VISIBLE);
                }

                if (best.getCoverImage() != null) {
                    holder.iv_cast.setImageUrl(best.getCoverImage().getImgUrl(), AppController.getInstance().getImageLoader());
                } else {
                    holder.iv_cast.setDefaultImageResId(R.drawable.loading_335_235_defualt);
                }

                holder.tv_title.setText(best.getTitle());

                String []prize = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th", "9th"};
                if(position != 0) {
                    holder.tv_number.setText(prize[position]);
                }
                return convertView;
            }
        });

        m_lvBestCast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startCastDetailActivity(m_arrBestCast.get(i));
            }
        });

        requestBestCastList();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.title_activity_cast));

        ImageButton backBtn = (ImageButton)view.findViewById(R.id.ib_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void startCastDetailActivity(Cast cast){
        Intent intent = new Intent(this, CastDetailActivity.class);

        intent.putExtra("cast_id",cast.getId());
        startActivity(intent);
    }

    private void requestBestCastList() {
        String url = ServerAPIPath.API_GET_BEST_CAST;
        url += "?cnt="+MAX_BEST_CNT;

        ServerAPICall.getInstance().callGET(url,
                new onReponseListener() {
                    @Override
                    public void onResponse(Object o) {
                        try {
                            m_arrBestCast.clear();

                            JSONArray dataArray = (JSONArray)o;

                            int cnt = dataArray.length();
                            for(int i = 0; i < cnt; i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                Cast advertise = new Cast(jsonObject);

                                m_arrBestCast.add(advertise);
                            }

                            ((ArrayAdapter<Cast>)m_lvBestCast.getAdapter()).notifyDataSetChanged();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                            Date date = new Date();
                            m_tvTime.setText(dateFormat.format(date)+".Update");

                        } catch (Exception e) {
                            Logger.e(TAG, "responseBestCastList - JSONException : " + e.getMessage());
                            Toast.makeText(CastBestActivity.this, ResponseMessage.ERR_INVALID_DATA, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new onErrorListener() {
                    @Override
                    public void onErrorResponse(String s) {
                        Toast.makeText(CastBestActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
