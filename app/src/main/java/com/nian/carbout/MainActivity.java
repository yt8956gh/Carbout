package com.nian.carbout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.nian.carbout.news.NewsActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<co2_item> items;

    //不能在onCreat方法前使用findViewById，因為這時VIEW還沒建構完成

    private int statusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFab();

        Toast toast = Toast.makeText(this,"ENTER",Toast.LENGTH_SHORT);
        toast.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.Add_fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        //設定清單中的普通item的顏色
        int sheetColor = getResources().getColor(R.color.background_card);
        //設定清單中的醒目item的顏色
        int fabColor = getResources().getColor(R.color.colorPrimary);

        // Create material sheet FAB
        MaterialSheetFab materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

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

        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.fab_sheet_item_transport:
                        startActivity(new Intent(v.getContext(),Transport_Activity.class));
                        break;
                    case R.id.fab_sheet_item_shopping:
                        break;
                    case R.id.fab_sheet_item_power:

                        break;
                    case R.id.fab_sheet_item_service:

                        break;
                    case R.id.fab_sheet_item_trash:

                        break;
                    case R.id.fab_sheet_item_self:

                        break;
                }
            }
        };

        // Set material sheet item click listeners
        findViewById(R.id.fab_sheet_item_transport).setOnClickListener(handler);
        findViewById(R.id.fab_sheet_item_shopping).setOnClickListener(handler);
        findViewById(R.id.fab_sheet_item_service).setOnClickListener(handler);
        findViewById(R.id.fab_sheet_item_power).setOnClickListener(handler);
        findViewById(R.id.fab_sheet_item_trash).setOnClickListener(handler);
        findViewById(R.id.fab_sheet_item_self).setOnClickListener(handler);
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
            startActivity(new Intent(MainActivity.this, AnalysisActivity.class));
        } else if (id == R.id.nav_grade) {

        } else if (id == R.id.nav_info) {
            startActivity(new Intent(MainActivity.this, NewsActivity.class));
        } else if (id == R.id.nav_list) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
