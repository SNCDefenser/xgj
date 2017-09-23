package com.xgj.app.xgj;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xgj.app.mylibrary.*;

import java.util.ArrayList;
import java.util.List;

public class AddStock extends AppCompatActivity {

    List<String> tags = new ArrayList<>();
    FlowTagLayout mFlowTagLayout_tag;
    TagAdapter<String> mTagAdapter_tag;
    List<String> expire = new ArrayList<>();
    FlowTagLayout mFlowTagLayout_expire;
    TagAdapter<String> mTagAdapter_expire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tv_tag = (TextView) findViewById(R.id.textView_tag);
        tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddStock.this,"You clicked：tag",Toast.LENGTH_SHORT).show();
            }
        });
        //tags
        mFlowTagLayout_tag = (FlowTagLayout) findViewById(R.id.tag_flow_layout);
        mTagAdapter_tag = new TagAdapter<>(this);
        mFlowTagLayout_tag.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mFlowTagLayout_tag.setAdapter(mTagAdapter_tag);
        mFlowTagLayout_tag.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    Snackbar.make(parent, "Tags:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "No selected tag", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        initTags();
        //expire
        mFlowTagLayout_expire = (FlowTagLayout) findViewById(R.id.expire_flow_layout);
        mTagAdapter_expire = new TagAdapter<>(this);
        mFlowTagLayout_expire.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mFlowTagLayout_expire.setAdapter(mTagAdapter_expire);
        mFlowTagLayout_expire.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                    }
                    Snackbar.make(parent, "Tags:" + sb.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "No selected tag", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        initExpire();
    }
    private void initTags() {
        tags.add("Love");
        tags.add("Fish");
        tags.add("Food");
        tags.add("Snack");
        tags.add("Fruit");
        tags.add("Meat");
        tags.add("Medicine");
        tags.add("Makeup");
        tags.add("Easy");
        tags.add("Great");
        mTagAdapter_tag.onlyAddAll(tags);
    }
    private void initExpire() {
        expire.add("5 days");
        expire.add("1 week");
        expire.add("2 weeks");
        expire.add("1 month");
        expire.add("3 month");
        expire.add("1 year");
        mTagAdapter_expire.onlyAddAll(expire);
    }
}
