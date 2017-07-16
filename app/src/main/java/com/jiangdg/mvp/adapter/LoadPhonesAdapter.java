package com.jiangdg.mvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangdg.mvp.R;
import com.jiangdg.mvp.bean.PhoneInfoBean;

import java.util.List;

/**手机商品列表适配器
 *
 * Created by jianddongguo on 2017/7/4.
 * http://blog.csdn.net/andrexpert
 */

public class LoadPhonesAdapter extends RecyclerView.Adapter<LoadPhonesAdapter.ViewHolder>{
    private Context mContext;
    private List<PhoneInfoBean> mDataList;

    // ViewHolder类复用view
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivPhoneIcon;
        private TextView tvPhoneName;
        private TextView tvPhonePrices;

        public ViewHolder(View rootView) {
            super(rootView);
            ivPhoneIcon = (ImageView) rootView.findViewById(R.id.iv_item_phone_icon);
            tvPhoneName = (TextView)rootView.findViewById(R.id.tv_item_phone_name);
            tvPhonePrices = (TextView)rootView.findViewById(R.id.tv_item_phone_prices);
        }
    }

    // 为是适配器设置数据
    public void setAdapterData(List<PhoneInfoBean> mDataList){
        this.mDataList = mDataList;
    }

    // 绑定item的view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.list_item_phone_infos,null);
        return new ViewHolder(rootView);
    }

    // 绑定数据到item
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PhoneInfoBean bean = mDataList.get(position);
        if(viewHolder != null || bean != null){
            viewHolder.ivPhoneIcon.setImageResource(bean.getPhonePic());
            viewHolder.tvPhoneName.setText(bean.getPhoneName());
            viewHolder.tvPhonePrices.setText("¥ "+bean.getPhonePrice());
        }
    }

    // item的数目
    @Override
    public int getItemCount() {
        if(mDataList == null){
            return 0;
        }
        return mDataList.size();
    }
}
