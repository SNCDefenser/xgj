package com.xgj.app.xgj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private List<Item> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        initButton();
        initItem();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.Recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
                GridLayout grid = (GridLayout) findViewById(R.id.gridLayout);
                if(grid.getVisibility() ==View.VISIBLE) {
                    grid.setVisibility(View.GONE);
                }else{
                    grid.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
