package com.nian.carbout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.nian.carbout.analysis.AnalysisActivity;
import com.nian.carbout.commodity.CommodityActivity;
import com.nian.carbout.energy.EnergyActivity;
import com.nian.carbout.grade.GradeActivity;
import com.nian.carbout.news.NewsActivity;
import com.nian.carbout.commodity_Search.CommoditySearchActivity;
import com.nian.carbout.self.SelfActivity;
import com.nian.carbout.service.ServiceActivity;
import com.nian.carbout.transport.Transport_Activity;
import com.nian.carbout.waste.WasteActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseOutBack;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //不能在onCreat方法前使用findViewById，因為這時VIEW還沒建構完成

    private int statusBarColor;
    private int[] usage = new int[7];
    final ArrayList<String> week = new ArrayList<>();
    private Integer[] date_of_week = new Integer[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        setupFab();

        setupWeek();

        setupTodayCo2();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        toolbar.setTitle(" ");
        toolbar.setTitleTextColor(getResources().getColor(R.color.background_card));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupWeek();
        setupChart();
        setupTodayCo2();
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
                        startActivity(new Intent(v.getContext(), CommodityActivity.class));
                        break;

                    case R.id.fab_sheet_item_power:
                        startActivity(new Intent(v.getContext(), EnergyActivity.class));
                        break;

                    case R.id.fab_sheet_item_service:
                        startActivity(new Intent(v.getContext(), ServiceActivity.class));
                        break;

                    case R.id.fab_sheet_item_trash:
                        startActivity(new Intent(v.getContext(), WasteActivity.class));
                        break;

                    case R.id.fab_sheet_item_self:
                        startActivity(new Intent(v.getContext(), SelfActivity.class));
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


    private void setupTodayCo2()
    {
        int today_usage = usage[6]/1000;

        TextView today_co2 = findViewById(R.id.today_co2);
        today_co2.setText(String.valueOf(today_usage));
        today_co2.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout circle = findViewById(R.id.circle_in_main);


        if(today_usage>=20 && today_usage<50)//黃燈
        {
            circle.setBackgroundResource(R.drawable.circle_shadow);
        }
        else if(today_usage>=50)//紅燈
        {
            circle.setBackgroundResource(R.drawable.circle_serious);
        }
        else
        {
            circle.setBackgroundResource(R.drawable.circle);
        }


    }

    private void setupWeek()
    {
        Calendar cal = Calendar.getInstance();
        Date dNow;
        String[] week_zh = new String[]{"日","一","二","三","四","五","六"};
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        //取得星期幾的整數值
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_YEAR,-7);

        week.clear();


        //運算星期與七天內的日期
        for(int i=0;i<7;i++)
        {
            week.add(week_zh[dayOfWeek%7]);
            dayOfWeek++;

            cal.add(Calendar.DAY_OF_YEAR,1);//得到cal+1的日期
            dNow = cal.getTime();//從calender得到data實例
            date_of_week[i] = Integer.parseInt(dateFormatter.format(dNow));//輸出特殊規格的日期
        }

        for(int i=0;i<7;i++)
        {
            Log.d("day_of_week" , date_of_week[i]+"");
        }
    }

    private void setupChart()
    {

        DataBaseHelper dataHelper;
        SQLiteDatabase db;
        int date_tmp,co2_tmp;


        BarChart chart = (BarChart)findViewById(R.id.chart_line);


        //處理SQLite相關資料
        dataHelper = new DataBaseHelper(this, "co2.sqlite",null, 1);
        db = dataHelper.getWritableDatabase();

        //依據條件搜尋SQLite，並回傳資料指標
        Cursor c = db.rawQuery("SELECT * FROM main WHERE date >="+date_of_week[0], null);
        c.moveToFirst();

        //usage歸零，以免加到上一次的數據
        for(int i=0;i<7;i++)
        {
            usage[i]=0;
        }


        //運算七天內的co2數據
        for(int i = 0; i < c.getCount(); i++) {

            date_tmp =  c.getInt(1);
            co2_tmp = c.getInt(3);

            usage[Arrays.asList(date_of_week).indexOf(date_tmp)]+=co2_tmp;//直接用值取得INDEX

            c.moveToNext();//移動至下一筆
        }

        c.close();//關閉SQLite指標

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //xAxis.setValueFormatter(formatter);

        List entries = new ArrayList<>();

        for(int i=0;i<7;i++)
        {
            entries.add(new BarEntry(i, usage[i]/1000));
        }

        BarDataSet set = new BarDataSet(entries, null);

        //ColorSet只接受color.rgb型態的顏色敘述
        set.setColors(Color.rgb(0, 88, 122),
                Color.rgb(0, 136, 145));

        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(0, 88, 122));

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);//將右下角的方塊拿掉
        configChartAxis(chart);
        chart.animateXY(1000,1500);
        chart.invalidate(); // refresh
    }

    //設定chart線條格式
    private void configChartAxis(BarChart chart_bar){

        //重寫x軸欄位
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return week.get((int) value);
            }

        };

        XAxis xAxis = chart_bar.getXAxis();
        xAxis.setValueFormatter(formatter);
        //xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = chart_bar.getAxisLeft();
        //leftYAxis.setGranularity(60);
        leftYAxis.setEnabled(false);

        YAxis RightYAxis = chart_bar.getAxisRight();
        RightYAxis.setEnabled(false);//不顯示右側
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
            startActivity(new Intent(MainActivity.this,GradeActivity.class));

        } else if (id == R.id.nav_info) {
            startActivity(new Intent(MainActivity.this, NewsActivity.class));

        } else if (id == R.id.nav_list) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
