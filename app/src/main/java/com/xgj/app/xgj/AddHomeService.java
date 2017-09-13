package com.xgj.app.xgj;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidaxia.mylibrary.FlowTagLayout;
import com.lidaxia.mylibrary.OnTagSelectListener;

import java.util.ArrayList;
import java.util.List;

public class AddHomeService extends AppCompatActivity {

    List<String> tags = new ArrayList<>();
    FlowTagLayout mFlowTagLayout_tag;
    TagAdapter<String> mTagAdapter_tag;
    List<String> frequency = new ArrayList<>();
    FlowTagLayout mFlowTagLayout_frequency;
    TagAdapter<String> mTagAdapter_frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home_service);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView set_date = (TextView) findViewById(R.id.textView_next_service_date);
        set_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(AddHomeService.this,"You clicked：set date",Toast.LENGTH_SHORT).show();
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
        mFlowTagLayout_frequency = (FlowTagLayout) findViewById(R.id.frequency_flow_layout);
        mTagAdapter_frequency = new TagAdapter<>(this);
        mFlowTagLayout_frequency.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mFlowTagLayout_frequency.setAdapter(mTagAdapter_frequency);
        mFlowTagLayout_frequency.setOnTagSelectListener(new OnTagSelectListener() {
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
        initFrequency();

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
    private void initFrequency() {
        frequency.add("Day");
        frequency.add("Week");
        frequency.add("Month");
        frequency.add("Year");
        mTagAdapter_frequency.onlyAddAll(frequency);
    }
}
