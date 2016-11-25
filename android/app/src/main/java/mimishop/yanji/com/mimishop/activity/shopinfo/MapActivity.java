package mimishop.yanji.com.mimishop.activity.shopinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.Shop;


public class MapActivity extends FragmentActivity {
    private Context context;
    private GoogleMap googleMap;
    private Shop  shop = new Shop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (savedInstanceState == null) {

        }

        String shopName = getIntent().getStringExtra("shopName");
        double shopLongtitude = getIntent().getDoubleExtra("shopLongtitude", 0.0);
        double shopLatitude = getIntent().getDoubleExtra("shopLatitude", 0.0);
        shop.setShopLongtitude(shopLongtitude);
        shop.setShopLatitude(shopLatitude);
        shop.setName(shopName);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng latLng = new LatLng(shop.getShopLatitude(), shop.getShopLongtitude());
        MarkerOptions marker = new MarkerOptions().position(latLng).title(shop.getName());
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location));
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        googleMap.setMyLocationEnabled(true);

        initMenu();
    }

    private void initMenu() {
        View view = findViewById(R.id.menu_main);
        ImageButton backBtn = (ImageButton)view.findViewById(R.id.btn_back);

        if(backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        TextView title = (TextView)view.findViewById(R.id.tv_title);
        title.setText(shop.getName());
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}