package mimishop.yanji.com.mimishop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mimishop.yanji.com.mimishop.R;
import mimishop.yanji.com.mimishop.activity.mypage.MyShopManageActivity;
import mimishop.yanji.com.mimishop.constant.ResponseMessage;
import mimishop.yanji.com.mimishop.modal.Product;

/**
 * Created by KimCholJu on 5/22/2015.
 */
public class AddProductDialog extends Dialog {

    private Context mContext;
    private Product mProduct;
    private EditText m_etProductName;
    private EditText m_etRightPrice;
    private EditText m_etDiscountPrice;

    public AddProductDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
    }

    public AddProductDialog(Context context, Product product) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        mContext = context;
        mProduct = product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.6f;
        getWindow().setAttributes(lpWindow);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.dlg_add_product);
        m_etProductName = (EditText)findViewById(R.id.et_product_name);
        m_etRightPrice = (EditText)findViewById(R.id.et_right_price);
        m_etDiscountPrice = (EditText)findViewById(R.id.et_discount_price);

        Button btnOK = (Button)findViewById(R.id.btn_add_product);
        if(mProduct != null) {
            m_etProductName.setText(mProduct.getName());
            m_etRightPrice.setText(mProduct.getPrice());
            m_etDiscountPrice.setText(mProduct.getEventPrice());
            ((TextView)findViewById(R.id.tv_dlg_title)).setText("상품 변경");
            btnOK.setText("상품 변경");
        }

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_etProductName.getText().toString().equals("") == false){

                    Product product = null;

                    if(mProduct != null) {
                        product = mProduct;
                    }
                    else {
                        product = new Product();
                        product.setNewProduct();
                    }

                    product.setName(m_etProductName.getText().toString());
                    product.setPrice(m_etRightPrice.getText().toString());
                    product.setEventPrice(m_etDiscountPrice.getText().toString());

                    if(mContext.getClass() == MyShopManageActivity.class) {
                        if(mProduct != null) {
                            ((MyShopManageActivity)mContext).modifyProduct(product);
                        }
                        else{
                            ((MyShopManageActivity) mContext).addPrice(product);
                        }
                    }

                    dismiss();
                }
                else {
                    Toast.makeText(mContext, ResponseMessage.ERR_NO_PRODUCT_NAME, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton btnExit = (ImageButton)findViewById(R.id.ib_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        if(true) {
            initKeyboardTouch();
        }
    }


    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    private  void initKeyboardTouch() {

        View view = findViewById(R.id.root);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
        view = findViewById(R.id.content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
            }
        });
    }
}
