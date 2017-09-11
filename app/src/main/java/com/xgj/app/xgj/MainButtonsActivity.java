package com.xgj.app.xgj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yujiezhang on 9/9/17.
 */

public class MainButtonsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainbuttons);
    }

    public void initButton(){
        Button shoppingList = (Button) findViewById(R.id.shopping_List);
        shoppingList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainButtonsActivity.this,Activity.class);
                startActivity(intent);
            }
        });
    }
}
