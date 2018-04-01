package com.nian.carbout;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Home_item> items;

    //不能在onCreat方法前使用findViewById，因為這時VIEW還沒建構完成
    private RecyclerView item_list;
    private RVAdapter itemAdapter;
    private RecyclerView.LayoutManager rLayoutManager;
    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeData();

        Toast toast = Toast.makeText(this,"ENTER",Toast.LENGTH_SHORT);
        toast.show();

        item_list = (RecyclerView)findViewById(R.id.rv);

        items = new ArrayList<>();
        items.add(new Home_item("麥香紅茶250ml","2018/2/9",100,1));
        items.add(new Home_item("泡麵","2018/2/12",220,2));
        items.add(new Home_item("鳳梨酥","2018/2/13",170,3));

        //將數量設為可變
        item_list.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(this);
        item_list.setLayoutManager(rLayoutManager);


        itemAdapter = new RVAdapter(items, this) {
            @Override
            public void onBindViewHolder(final ViewHolder holder,
                                        final int position) {
                super.onBindViewHolder(holder, position);

                // 建立與註冊項目點擊監聽物件
                holder.rootView.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // 讀取選擇位置的項目物件
                                Home_item item = items.get(position);
                                // 顯示項目資訊
                            }
                        }
                );
            }
        };

        // 設定RecyclerView使用的資料來源物件
        //item_list.setAdapter(itemAdapter);
        // 建立與設定項目操作物件

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallBack(itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(item_list);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Item Successfully !!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                clickAdd(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        //findViewById(R.id.fab_sheet_item_recording).setOnClickListener(this);
        //findViewById(R.id.fab_sheet_item_reminder).setOnClickListener(this);
        //findViewById(R.id.fab_sheet_item_photo).setOnClickListener(this);
        //findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_analysis) {
            // Handle the camera action
        } else if (id == R.id.nav_grade) {

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_tool) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clickAdd(View view) {
        // 決定新項目的編號
        int newId = items.size() + 1;
        Date date = new Date();
        // 建立新增項目物件
        Home_item fruit = new Home_item(
                Integer.toString(newId), date.toString(),newId*10, newId);
        // 新增一個項目
        itemAdapter.add(fruit);
        // 控制列表元件移到最後一個項目
        item_list.scrollToPosition(items.size() - 1);
    }

    private void InitializeData(){
        items = new ArrayList<>();
        items.add(new Home_item("麥香紅茶250ml","2018/2/9",100,1));
        items.add(new Home_item("泡麵","2018/2/12",220,2));
        items.add(new Home_item("鳳梨酥","2018/2/13",170,3));
    }
}
