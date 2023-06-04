package com.darjeeling.android52_btvnday8;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvDemo;
    private ArrayList<Product> mListProduct;
    private ProductAdapter mProductAdapter;
    private SqliteHelper mSqliteHelper;
    private Button btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        btnadd = findViewById(R.id.btnadd);
        initData();
        initView();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_add.class);
                startAddResultLauncher.launch(intent);

            }
        });

    }

    ActivityResultLauncher<Intent> startAddResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK){
                mListProduct = null;
                mListProduct = new ArrayList<>();
                mListProduct.addAll(mSqliteHelper.getListProduct());
                mProductAdapter.updateData(mListProduct);
            }
        }
    });

    private void initView() {

        rvDemo = findViewById(R.id.rvDemo);
        mProductAdapter = new ProductAdapter(mListProduct);
        mProductAdapter.setCallback(clickListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvDemo.setLayoutManager(linearLayoutManager);
        rvDemo.setHasFixedSize(true);
//        rvDemo.setItemViewCacheSize(10);
        rvDemo.setAdapter(mProductAdapter);

    }

    private IItemClickListener clickListener = new IItemClickListener() {
        @Override
        public void onItemClick(int pos) {

        }

        @Override
        public void onChangeWishList(int position) {
            Product productModel = mListProduct.get(position);
            productModel.setWish(!productModel.isWish());
            mListProduct.set(position, productModel);
//            mProductAdapter.notifyDataSetChanged();
            mProductAdapter.notifyItemChanged(position);
        }

        @Override
        public void onDelete(int position) {
            mListProduct.remove(position);
//            mProductAdapter.notifyDataSetChanged();
            mProductAdapter.notifyItemRemoved(position);

        }

        @Override
        public void onUpdate(int position) {
            Product productModel = mListProduct.get(position);
            productModel.setTitle(productModel.getTitle() + " new");
            mListProduct.set(position, productModel);
            mProductAdapter.notifyDataSetChanged();
        }
    };

    private void initData() {
        mSqliteHelper = new SqliteHelper(this);
        mListProduct = mSqliteHelper.getListProduct();
    }


}