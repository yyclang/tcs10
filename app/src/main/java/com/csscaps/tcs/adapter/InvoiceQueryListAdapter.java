package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

/**
 * Created by tl on 2018/5/30.
 */

public class InvoiceQueryListAdapter extends BaseManagementListAdapter<Invoice> {


    public InvoiceQueryListAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Invoice item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.invoice_code, item.getInvoice_type_code());
        helper.setText(R.id.invoice_no, item.getInvoice_no());
        helper.setText(R.id.issued_by, item.getDrawer_name());
        helper.setText(R.id.tax_Amount, String.format("%.2f",(Double.valueOf(item.getTotal_tax_due())- Double.valueOf(item.getTotal_fee()))));
        helper.setText(R.id.i_tax, String.format("%.2f",(Double.valueOf(item.getTotal_all())- Double.valueOf(item.getTotal_fee()))));
        helper.setText(R.id.upload_status, item.getUploadStatus().equals("1") ? "Y" : "N");
        helper.setText(R.id.issuing_date_time, DateUtils.dateToStr(DateUtils.getStringToDate(item.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
        //AVL:normal, DISA:cancelled, NEG:negative, WRO:wrote off, IVLD:invalid, IRR:illegal
        switch (item.getStatus()) {
            case "AVL":
                helper.setText(R.id.status, context.getResources().getString(R.string.normal));
                break;
            case "DISA":
                helper.setText(R.id.status, context.getResources().getString(R.string.cancelled));
                break;
            case "NEG":
                helper.setText(R.id.status, context.getResources().getString(R.string.negative));
                break;
            case "WRO":
                helper.setText(R.id.status, context.getResources().getString(R.string.wrote_off));
                break;
            case "IVLD":
                helper.setText(R.id.status, context.getResources().getString(R.string.invalid));
                break;
            case "IRR":
                helper.setText(R.id.status, context.getResources().getString(R.string.illegal));
                break;

        }
    }
}
