
package com.zhuanghongji.mpchartexample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.zhuanghongji.mpchartexample.custom.MyMarkerView;
import com.zhuanghongji.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;

import butterknife.BindView;

public class BarChartActivityMultiDataset extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.chart1)
    BarChart mChart;

    @BindView(R.id.seekBar1)
    SeekBar mSeekBarX;

    @BindView(R.id.seekBar2)
    SeekBar mSeekBarY;

    @BindView(R.id.tvXMax)
    TextView tvX;

    @BindView(R.id.tvYMax)
    TextView tvY;

    @SuppressWarnings("ButterKnifeInjectNotCalled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChart.getDescription().setEnabled(false);

//        mChart.setDrawBorders(true);
        
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        mSeekBarX.setProgress(10);
        mSeekBarY.setProgress(100);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(mTfLight);
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_barchart;
    }

    @Override
    protected void initViews() {
        setupToolbar(mToolbar,R.string.ci_13_name,R.string.ci_13_desc,R.menu.bar,true);
    }

    @Override
    protected void initEvents() {
        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY.setOnSeekBarChangeListener(this);
        mChart.setOnChartValueSelectedListener(this);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionToggleValues: {
                        for (IBarDataSet set : mChart.getData().getDataSets())
                            set.setDrawValues(!set.isDrawValuesEnabled());

                        mChart.invalidate();
                        break;
                    }
                    case R.id.actionTogglePinch: {
                        if (mChart.isPinchZoomEnabled())
                            mChart.setPinchZoom(false);
                        else
                            mChart.setPinchZoom(true);

                        mChart.invalidate();
                        break;
                    }
                    case R.id.actionToggleAutoScaleMinMax: {
                        mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                        mChart.notifyDataSetChanged();
                        break;
                    }
                    case R.id.actionToggleBarBorders: {
                        for (IBarDataSet set : mChart.getData().getDataSets())
                            ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                        mChart.invalidate();
                        break;
                    }
                    case R.id.actionToggleHighlight: {
                        if(mChart.getData() != null) {
                            mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                            mChart.invalidate();
                        }
                        break;
                    }
                    case R.id.actionSave: {
                        // mChart.saveToGallery("title"+System.currentTimeMillis());
                        mChart.saveToPath("title" + System.currentTimeMillis(), "");
                        break;
                    }
                    case R.id.animateX: {
                        mChart.animateX(3000);
                        break;
                    }
                    case R.id.animateY: {
                        mChart.animateY(3000);
                        break;
                    }
                    case R.id.animateXY: {

                        mChart.animateXY(3000, 3000);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x3 dataset
        float barWidth = 0.3f; // x3 dataset
        // (0.3 + 0.02) * 3 + 0.04 = 1.00 -> interval per "group"

        int startYear = 1980;
        int endYear = startYear + mSeekBarX.getProgress();

        tvX.setText(startYear + "\n-" + endYear);
        tvY.setText("" + (mSeekBarY.getProgress()));

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        float mult = mSeekBarY.getProgress() * 100000f;

        for (int i = startYear; i < endYear; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals1.add(new BarEntry(i, val));
        }

        for (int i = startYear; i < endYear; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals2.add(new BarEntry(i, val));
        }

        for (int i = startYear; i < endYear; i++) {
            float val = (float) (Math.random() * mult) + 3;
            yVals3.add(new BarEntry(i, val));
        }

        BarDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet)mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet)mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create 3 datasets with different types
            set1 = new BarDataSet(yVals1, "Company A");
            // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
            // ColorTemplate.FRESH_COLORS));
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "Company C");
            set3.setColor(Color.rgb(242, 247, 158));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new LargeValueFormatter());

            // add space between the dataset groups in percent of bar-width
            data.setValueTypeface(mTfLight);

            mChart.setData(data);
        }

        mChart.getBarData().setBarWidth(barWidth);
        mChart.getXAxis().setAxisMinimum(startYear);
        mChart.getXAxis().setAxisMaximum(mChart.getBarData().getGroupWidth(groupSpace, barSpace) * mSeekBarX.getProgress() + startYear);
        mChart.groupBars(startYear, groupSpace, barSpace);
        mChart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
