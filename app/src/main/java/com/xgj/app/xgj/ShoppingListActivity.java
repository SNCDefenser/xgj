package com.xgj.app.xgj;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.lidaxia.mylibrary.FlowTagLayout;
import com.lidaxia.mylibrary.OnTagSelectListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private List<Item> itemList = new ArrayList<>();
    List<String> tags = new ArrayList<>();
    FlowTagLayout mFlowTagLayout;
    TagAdapter<String> mTagAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        initButton();
        initItem();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        createTag();
        recyclerView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }

    private void initItem(){
        for(int i = 0; i < 1; i++){
            Item apple = new Item("Apple", R.drawable.ic_account_circle_black_24px);
            Item banana = new Item("Banana", R.drawable.ic_account_circle_black_24px);
            itemList.add(apple);
            itemList.add(banana);
        }
    }
    private void initButton(){
        Button tag = (Button) findViewById(R.id.Tag);
        tag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LinearLayout tag = (LinearLayout) findViewById(R.id.tagLayout);
                if(tag.getVisibility() ==View.VISIBLE) {
                    tag.setVisibility(View.GONE);
                }else{
                    tag.setVisibility(View.VISIBLE);
                }
            }
        });
        /*Add item into shoppinglist*/
        Button addtoSL = (Button) findViewById(R.id.addItem);
        addtoSL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, AddShoppingList.class);
                startActivity(intent);
            }
        });
    }
    private void createTag(){
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
