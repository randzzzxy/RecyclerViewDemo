package com.example.recyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    int count = 500000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new MyAdapter(this));
    }


    private class MyAdapter implements RecyclerView.Adapter {
        LayoutInflater layoutInflater;
        private int height;
        public MyAdapter(MainActivity mainActivity) {
            height = 80;
            layoutInflater = mainActivity.getLayoutInflater();
        }

        @Override
        public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.item_layout,parent);
            }
            return convertView;
        }

        @Override
        public View onBindViewHolder(int position, View convertView, ViewGroup parent) {
            TextView textView = convertView.findViewById(R.id.textview);
            textView.setText("第" + position + "行");
            return convertView;
        }

        @Override
        public int getItemViewType(int row) {
            return 0;
        }

        @Override
        public int getItemTypeCount() {
            return 1;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public int getHeight(int index) {
            return 0;
        }
    }
}