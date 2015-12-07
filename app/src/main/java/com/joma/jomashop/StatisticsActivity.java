package com.joma.jomashop;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.joma.jomashop.dto.StatisticDTO;
import com.joma.jomashop.utils.WebParser;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private final String  apiUrl = "http://www.gana.sk/jomashop.html";
    private RelativeLayout layout;
    private PieChart mChart;
    private StatisticDTO[] products;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        LoadStatisticsTask task = new LoadStatisticsTask();
        task.execute();
    }

    public void initMchart() {
        layout = (RelativeLayout) findViewById(R.id.statisticsLayout);
        mChart = new PieChart(this);

        setContentView(mChart);
        layout.setBackgroundColor(Color.LTGRAY);
        addData();

        mChart.setUsePercentValues(true);
        mChart.setDescription("Smartphones");
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    public void addData() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i=0;i<products.length;i++) {
            yVals.add(new Entry(products[i].getTotal(), i));
        }

        for (int i=0;i<products.length;i++) {
            xVals.add(products[i].getName());
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Market Share");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for(int c: ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for(int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for(int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for(int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        mChart.highlightValues(null);

        mChart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
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

    public StatisticDTO[]  parseStatistics(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, StatisticDTO[].class);
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void showErrorMessage() {
        Toast.makeText(StatisticsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void setProducts(StatisticDTO[] products) {
        this.products = products;
    }

    public StatisticDTO[] getProducts() {
        return this.products;
    }

    /**
     * Created by mgana on 07.12.2015.
     */
    private class LoadStatisticsTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progress;

        @Override
        protected Void doInBackground(String... params) {

            WebParser webParser = new WebParser(apiUrl);

            String jsonString = webParser.getPage();


            if(jsonString == null) {
                setErrorMessage(getResources().getString(R.string.statisticsError));
                return null;
            }

            setProducts(parseStatistics(jsonString));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(errorMessage != null) {
                showErrorMessage();
                return;
            }

            initMchart();
        }
    }
}
