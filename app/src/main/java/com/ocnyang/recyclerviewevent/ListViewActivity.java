package com.ocnyang.recyclerviewevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import com.ocnyang.recyclerviewevent.recyembellish.DividerListItemDecoration;
import com.ocnyang.recyclerviewevent.recyevent.OnRecyclerItemClickListener;
import com.ocnyang.recyclerviewevent.recyevent.RecyItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<String> mStringList;
    RecyAdapter mRecyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recy);
        initRecy();
    }

    private void initRecy() {
        if (mStringList == null) {
            mStringList = new ArrayList<>();
        }
        mStringList.addAll(DataManager.getData(15 - mStringList.size()));
        mRecyAdapter = new RecyAdapter(R.layout.item_listview, mStringList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerListItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        /**
         * 关键代码，触摸事件的设置
         */
        RecyItemTouchHelperCallback itemTouchHelperCallback = new RecyItemTouchHelperCallback(mRecyAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                RecyAdapter.ViewHolder viewHolder1 = (RecyAdapter.ViewHolder) viewHolder;
                String tvString = viewHolder1.mTextView.getText().toString();
                Toast.makeText(ListViewActivity.this, "单击~" + tvString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder viewHolder) {
                Toast.makeText(ListViewActivity.this, "" + "长按...", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mRecyAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
