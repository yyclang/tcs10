package com.csscaps.tcs.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.csscaps.common.DecimalDigitsInputFilter;
import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.AppTools;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/5/24.
 */

public class SelectProductListAdapter extends QuickAdapter<Product> implements TextWatcher {

    private List<Product> checkedList = new ArrayList<>();
    private InputFilter inputFilter[] = new InputFilter[]{new DecimalDigitsInputFilter(2)};
    private CheckBox mCheckBox;

    public SelectProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, final int position) {
        helper.setText(R.id.product_name, item.getProductName());
        helper.setText(R.id.specification, item.getSpecification());
        helper.setText(R.id.unit, item.getUnit());

        final EditText quantity = helper.getView(R.id.quantity);
        final EditText priceET = helper.getView(R.id.price);
        final CheckBox checkBox = helper.getView(R.id.checkbox);
        AppTools.expandViewTouchDelegate(checkBox, 100, 100, 100, 100);
        quantity.setFilters(inputFilter);
        priceET.setFilters(inputFilter);
        quantity.setText(item.getQuantity());

        String priceStr = item.getPrice();
        String unitDiscountPercentageStr = item.getUnitDiscountPercentage();
        String unitDiscountAmountStr = item.getUnitDiscountAmount();
        double price = TextUtils.isEmpty(priceStr) ? 0 : Double.valueOf(priceStr);
        double unitDiscountPercentage = TextUtils.isEmpty(unitDiscountPercentageStr) ? 0 : Double.valueOf(unitDiscountPercentageStr);
        double unitDiscountAmount = TextUtils.isEmpty(unitDiscountAmountStr) ? 0 : Double.valueOf(unitDiscountAmountStr);
        if (price != 0) {
            if (unitDiscountAmount != 0) {
                price = price - unitDiscountAmount;
            }

            if (unitDiscountPercentage != 0) {
                price = price * unitDiscountPercentage / 100;
            }

            price = Math.round(price * 100) * 0.01d;
        }

        priceET.setText(String.format("%.2f", price));
        if (TextUtils.isEmpty(item.getPrice())) {
            priceET.setEnabled(true);
        } else priceET.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedList.contains(item)) {
                        item.setPrice(priceET.getText().toString().trim());
                        item.setQuantity(quantity.getText().toString().trim());
                        checkedList.add(item);
//                        Logger.i("quantity " + quantity.getText().toString().trim());
//                        Logger.i("Price1 " + price.getText().toString().trim());
//                        Logger.i("name " + item.getProductName());
                    }

                } else {
                    checkedList.remove(item);
                }
            }
        });

        if (checkedList.contains(item)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }


        quantity.setOnFocusChangeListener(null);
        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mCheckBox = checkBox;
                }

            }
        });
        priceET.setOnFocusChangeListener(null);
        priceET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mCheckBox = checkBox;
                }
            }
        });

        priceET.removeTextChangedListener(this);
        quantity.removeTextChangedListener(this);
        priceET.addTextChangedListener(this);
        quantity.addTextChangedListener(this);
    }

    public List<Product> getCheckedList() {
        return checkedList;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mCheckBox != null && mCheckBox.isChecked()) {
            mCheckBox.setChecked(false);
            mCheckBox.setChecked(true);
        }
    }
}
