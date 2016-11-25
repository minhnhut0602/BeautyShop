package mimishop.yanji.com.mimishop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.modal.Product;

public class MyPriceAdapter extends ArrayAdapter<Product> {

    private Context context;

    private ViewHolder holder;
    private List<Product> eventList;
    private int resourceId;
    private int headerId = 0;

    private boolean isShowProductName = false;

    public MyPriceAdapter(Context context, int resourceId, List<Product> shopList) {
        super(context, resourceId, shopList);

        this.context = context;
        this.eventList = shopList;
        this.resourceId = resourceId;
    }

    public void setShowProductName(boolean isShow) {
        isShowProductName = isShow;
    }

    public void setHeaderId(int id) {
        headerId = id;
    }

    private class ViewHolder {
        private TextView         tv_product;
        private TextView         tv_right_price;
        private TextView         tv_discount_price;
        private EditText         et_product;
        private EditText         et_right_price;
        private EditText         et_discount_price;
        private View             fl_border_bottom;
        private View             rl_header;
        private View             rl_price;
    }

    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public Product getItem(int position) {
        if (eventList != null) {
            return eventList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (eventList != null) {
            return eventList.get(position).getId();
        }
        return 0;
    }

    public List<Product> getItemList() {
        return this.eventList;
    }

    public void setItemList(List<Product> _itemCardList) {
        this.eventList = _itemCardList;
    }

    @SuppressLint("WrongViewCast")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Product product = eventList.get(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(resourceId, null);

            if (resourceId == R.layout.item_row_my_price) {
                holder.tv_product = (TextView) convertView.findViewById(R.id.tv_product_name);
                holder.tv_right_price = (TextView) convertView.findViewById(R.id.tv_right_price);
                holder.tv_discount_price = (TextView) convertView.findViewById(R.id.tv_discount_price);
                holder.fl_border_bottom = convertView.findViewById(R.id.fl_border_bottom);
                holder.rl_header = convertView.findViewById(R.id.rl_header);
                holder.rl_price = convertView.findViewById(R.id.rl_price);
            } else if (resourceId == R.layout.item_row_register_product) {
                holder.et_product = (EditText) convertView.findViewById(R.id.et_product_name);
                holder.et_right_price = (EditText) convertView.findViewById(R.id.et_right_price);
                holder.et_discount_price = (EditText) convertView.findViewById(R.id.et_discount_price);
            }

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == 0 && resourceId == R.layout.item_row_my_price && headerId != 0)
        {
            holder.rl_price.setVisibility(View.GONE);
            holder.rl_header.setVisibility(View.VISIBLE);
            return convertView;
        }
        else {
            holder.rl_price.setVisibility(View.VISIBLE);
            holder.rl_header.setVisibility(View.GONE);
        }

        if(resourceId == R.layout.item_row_my_price) {
            if(holder.tv_product != null) {
                if (isShowProductName == false) {
                    holder.tv_product.setText(String.format("%d", product.getIndex()));
                } else {
                    holder.tv_product.setText(product.getName());
                }
            }

            if(holder.tv_right_price != null) {
                holder.tv_right_price.setText(product.getPrice());
            }

            if(holder.tv_discount_price != null) {
                holder.tv_discount_price.setText(product.getEventPrice());
            }

            if( holder.fl_border_bottom != null && position == (getCount() - 1)) {
                holder.fl_border_bottom.setVisibility(View.VISIBLE);
            }
        }
        else if(resourceId == R.layout.item_row_register_product) {
            if (product.getIndex() == 0) {
                holder.et_product.setText(product.getName());
                holder.et_product.setFocusable(true);
                holder.et_product.setFocusableInTouchMode(true);
                holder.et_product.setEnabled(true);
            }
            else {
                holder.et_product.setText("");
                holder.et_product.setFocusable(false);
                holder.et_product.setFocusableInTouchMode(false);
                holder.et_product.setEnabled(false);
            }

            holder.et_right_price.setText(product.getPrice());

            holder.et_discount_price.setText(product.getEventPrice());

            final EditText etProduct = holder.et_product;
            etProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                       product.setName(etProduct.getText().toString());
                    }
                }
            });

            final EditText etPrice  = holder.et_right_price;
            etPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        try {
                            product.setPrice(etPrice.getText().toString());
                        }catch (Exception e){

                        }

                    }
                }
            });

            final EditText etEventPrice = holder.et_discount_price;
            etEventPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        try {
                            product.setEventPrice(etEventPrice.getText().toString());
                        }catch (Exception e){

                        }
                    }
                }
            });
        }

        return convertView;
    }
}