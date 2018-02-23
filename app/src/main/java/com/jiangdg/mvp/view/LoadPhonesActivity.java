package com.jiangdg.mvp.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.jiangdg.mvp.R;
import com.jiangdg.mvp.view.adapter.LoadPhonesAdapter;
import com.jiangdg.mvp.bean.PhoneInfoBean;
import com.jiangdg.mvp.presenter.LoadPhonesPresenter;

import java.util.List;

/** 加载手机数据列表界面
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public class LoadPhonesActivity extends BaseActivity<ILoadPhonesView,LoadPhonesPresenter> implements ILoadPhonesView {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ProgressDialog mpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_phones);
        initView();
        // 请求数据
        mPresenter.fetchData();
    }

    @Override
    protected LoadPhonesPresenter createPresenter() {
        return new LoadPhonesPresenter();
    }

    private void initView() {
        // 初始化ToolBar
        mToolbar = (Toolbar)findViewById(R.id.toolbar_load_phones);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        mToolbar.setTitle("手机商品");
        setSupportActionBar(mToolbar);
//        ActionBar mActionBar = getSupportActionBar();
//        if(mActionBar != null){
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//            mActionBar.setDisplayShowHomeEnabled(true);
//        }
        // 初始化RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_phones);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置item分割线风格
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration(){});
    }

    @Override
    public void showProgress(String msg) {
        mpDialog = ProgressDialog.show(LoadPhonesActivity.this,"提示",msg);
    }

    @Override
    public void dismissProgress() {
        if(mpDialog != null){
            mpDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(LoadPhonesActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPhoneInfos(List<PhoneInfoBean> data) {
        LoadPhonesAdapter mAdapter = new LoadPhonesAdapter();
        mAdapter.setAdapterData(data);
        mRecyclerView.setAdapter(mAdapter);
    }
}
