package com.xgj.app.xgj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.xgj.app.xgj.R.id.frameLayout_shopping_cart;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String email = this.getIntent().getStringExtra("email");
        final Bundle bundle = new Bundle();
        bundle.putString("email", email);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Button btn_shopping_cart = (Button) findViewById(R.id.btn_shopping_cart);
        btn_shopping_cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, AddShoppingList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Button btn_stock = (Button) findViewById(R.id.btn_stock);
        btn_stock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, AddStock.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Button btn_home_service = (Button) findViewById(R.id.btn_home_service);
        btn_home_service.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, AddHomeService.class);
                startActivity(intent);
            }
        });
    }
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                Toast.makeText(this, "You clicked Back", Toast.LENGTH_SHORT).
                        show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
//测试失败 09/09
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_shopping_cart: {
//                Intent intent = new Intent(AddItem.this, AddShoppingList.class);
//                startActivity(intent);
//                break;
//            }
//            case R.id.btn_stock: {
//                break;
//            }
//            case R.id.btn_home_service: {
//                break;
//            }
//        }
//    }
}
