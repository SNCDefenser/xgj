package com.xgj.app.xgj;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by yujiezhang on 9/4/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Item> itemList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView itemImage;
        TextView itemName;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            itemName = (TextView) view.findViewById(R.id.item_name);
        }
    }
    public ListAdapter(List<Item> itemList){
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //Click item event, to manage this item;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View detail = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
                final PopupWindow itemDetail = new PopupWindow(detail,parent.getWidth(), parent.getHeight(), true);
                final int position = holder.getAdapterPosition();
                Item item = itemList.get(position);
                itemDetail.showAtLocation(detail, Gravity.CENTER, 0, 0);
                /*set name and image to popupwindow*/
                ImageView image = (ImageView) detail.findViewById(R.id.item_detail_image);
                image.setImageResource(item.getImageId());
                TextView name = (TextView) detail.findViewById(R.id.item_detail_name);
                CharSequence ch  = "";
                ch = ch + item.getName();
                name.setText(ch);
                /*Ok button, close the popup window*/
                Button dis = (Button) detail.findViewById(R.id.okButton);
                dis.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        itemDetail.dismiss();
                    }
                });
                /*Delete button, delete the item*/
                Button delete = (Button) detail.findViewById(R.id.deleteButton);
                delete.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        removeItem(position);
                        itemDetail.dismiss();
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemImage.setImageResource(item.getImageId());
        holder.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void initPopupWindow(){

    }

    public void removeItem(int position){
        itemList.remove(position);
        notifyItemRemoved(position);
    }
}
