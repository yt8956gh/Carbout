package com.nian.carbout.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.nian.carbout.R;
import com.nian.carbout.analysis.MyAdapter;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private ArrayList<news_item> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        data.add(new news_item("綠色商店哪裡找？",
                "你知道購物也能做環保嗎？\n\n若販售具環保標章或認證商品，政府會核發該商" +
                "家「綠色商店」標章，那如何尋找綠色商家呢？讓我們一探究竟吧！\n\n" +
                "迄今已超過1萬2千家販售3種以上環保標章產品，為方便民眾採購環保產品" +
                "，環保署於97年4月起開設綠色生活資訊網，其中的綠色商店系統，可查找政" +
                "府認證之綠色商店 。\n" +
                "馬上尋找身邊的綠色店家吧！", R.drawable.article_1));

        data.add(new news_item("祭祀更環保\n嘉義市以米代金",
                "傳統社會認為，燃燒越大量紙錢代表越虔誠，嘉義市環保局提倡敬神心誠則" +
                "靈，不燒紙錢或將紙錢減量，推廣讓民眾選擇使用平安米，替代「燒金」祈求" +
                "平安，十分有趣。\n\n" +
                "目前已有大天宮(五穀王廟)、朝天宮、南興宮及南恩禪寺合作推廣，若市內各" +
                "廟宇均響應以米代金，每年約可減少燃燒紙錢155公噸，減少二氧化碳排放量" +
                "232公噸，相當1萬9,298棵喬木年吸收量，相當可觀。\n\n" +
                "另外也宣導寺廟光明燈改用LED節能燈泡，迄今共52家寺廟響應，已換LED燈" +
                "泡23萬2,007顆，估計每年減少182萬度用電，節省電費380萬元，總計減少碳排" +
                "放量1,158噸，相當於9萬顆喬木年吸收量。\n\n" +
                "嘉義市環保局也為大家整理出環保寺廟，可以來此觀看更詳細的介紹 :\n" +
                "http://www.cycepb.gov.tw/Green/life_5.asp\n\n" +
                "下次大家去寺廟拜拜時，不妨也嘗試這些環保祭祀吧！", R.drawable.article_2));

        data.add(new news_item("古都電巴新紀元\n台南電動巴士營運",
                "古都台南電動巴士正式上路啦！經濟部輔導、臺南市政府推動、四方電巴公" +
                "司營運的「77路」公車於106年3月23日起正式通車營運。\n\n" +
                "公共運輸處處長黃耀國表示，77路電動巴士行駛時間為每日早上6點到下午10" +
                "點，平均班距約20～30分鐘，採一段票收費；行經臺南火車站、赤崁樓、成大醫" +
                "院、成大校園、海安商圈、百貨公司以及永華市政中心，無論觀光、洽公、通" +
                "勤都十分方便，希望各位多加利用。\n\n" +
                "詳情請見大台南公車網站 :\n" +
                "http://2384.tainan.gov.tw/NewTNBusWeb/", R.drawable.article_3));


        getSupportActionBar().hide();

        Toolbar toolbar = findViewById(R.id.toolbarNews);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewInNews);

        NewsAdapter adapter = new NewsAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
