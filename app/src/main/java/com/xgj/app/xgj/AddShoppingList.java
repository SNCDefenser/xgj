package com.xgj.app.xgj;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lidaxia.mylibrary.FlowTagLayout;
import com.lidaxia.mylibrary.OnTagSelectListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class AddShoppingList extends AppCompatActivity {

    List<String> places = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    List<String> selectedPlaces = new ArrayList<>();
    List<String> selectedTags = new ArrayList<>();
    FlowTagLayout mFlowTagLayout;
    TagAdapter<String> mTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);
        //back
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //tags
        mFlowTagLayout = (FlowTagLayout) findViewById(R.id.tag_flow_layout);
        mTagAdapter = new TagAdapter<>(this);
        mFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mFlowTagLayout.setAdapter(mTagAdapter);
        mFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
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

        //places
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("Please select a place");
        initPlaces();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,places);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddShoppingList.this,"You selected："+places.get(position),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initPlaces() {
        places.add("Walmart");
        places.add("Target");
        places.add("Ranch");
        places.add("Hmart");

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
        mTagAdapter.onlyAddAll(tags);
    }

}
