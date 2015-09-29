package com.mairos.ymoneytransfer.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mairos.ymoneytransfer.R;
import com.mairos.ymoneytransfer.storage.models.PaymentInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentInfoAdapter extends ArrayAdapter<PaymentInfo> {

    private Context mContext;
    private PaymentInfo[] mInfoArr;

    public PaymentInfoAdapter(Context context, PaymentInfo[] infoArr) {
        super(context, R.layout.payinfo_rowlayout, infoArr);
        mContext = context;
        mInfoArr = infoArr;
    }

    public void updateList(PaymentInfo[] infoArr){
        mInfoArr = infoArr;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mInfoArr.length;
    }

    @Override
    public PaymentInfo getItem(int position) {
        return mInfoArr[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.payinfo_rowlayout, null, true);
            holder = new ViewHolder();
            holder.payinfoAmount = (TextView) rowView.findViewById(R.id.payinfo_amount);
            holder.payinfoTime= (TextView) rowView.findViewById(R.id.payinfo_time);
            holder.payinfoPayee = (TextView) rowView.findViewById(R.id.payinfo_payee);
            holder.payinfoCode = (TextView) rowView.findViewById(R.id.payinfo_code);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        PaymentInfo pi = getItem(position);

        holder.payinfoAmount.setText(pi.getCreditAmount() + "p");
        DateFormat df = new SimpleDateFormat("HH:mm yyyy-MM-dd");
        holder.payinfoTime.setText(df.format(new Date(pi.getTime())));
        holder.payinfoPayee.setText(pi.getPayee());
        holder.payinfoCode.setText(pi.getProtectionCode());

        return rowView;
    }

    private static class ViewHolder {
        public TextView payinfoAmount;
        public TextView payinfoTime;
        public TextView payinfoPayee;
        public TextView payinfoCode;
    }
}
