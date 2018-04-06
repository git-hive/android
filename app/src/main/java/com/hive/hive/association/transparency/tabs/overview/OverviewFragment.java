package com.hive.hive.association.transparency.tabs.overview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyActivity;
import com.hive.hive.association.transparency.tabs.staff.StaffFragment;
import com.hive.hive.model.association.BudgetTransaction;
import com.hive.hive.model.association.BudgetTransactionCategories;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class OverviewFragment extends Fragment {

    private static final String TAG = OverviewFragment.class.getSimpleName();
    public static final String ARG_PAGE = "Resumo";

    //--- Context
    private TransparencyActivity mActivity;

    //--- Data
    private List<BudgetTransaction> incomes;
    private List<BudgetTransaction> expenses;


    //--- Chart
    private BarChart mMonthlyChart;



    public OverviewFragment() {
        // Required empty public constructor
    }


    public static OverviewFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
        fillDummyContent();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //--- Activity and Context
        mActivity = (TransparencyActivity) getActivity();

        //--- Views
        mMonthlyChart = view.findViewById(R.id.monthly_chart);


        //------------------------------------ Monthly Chart ------------------------------------//

        //--- Formatting Parameters
        float groupSpace = 0.2f;
        float barSpace = 0.03f;
        float barWidth = 0.02f;
        float monthsToShow = 12;

        //--------- Chart Data ---------
        //-- Instantiate arrays of BarEntries
        ArrayList<BarEntry> incomesEntries = new ArrayList<>();
        ArrayList<BarEntry> expensesEntries = new ArrayList<>();

        //-- Fill Incomes Entries
        for (int i = 0; i < monthsToShow && i < incomes.size(); i++)
            incomesEntries.add(new BarEntry(i, (int) incomes.get(i).getValue()));

        //-- Fill Expenses Entries
        for (int i = 0; i < monthsToShow && i < expenses.size(); i++)
            expensesEntries.add(new BarEntry(i, (int) expenses.get(i).getValue()));

        //-- Creates Income Dataset with entries
        BarDataSet incomeDataset = new BarDataSet(incomesEntries, "Receita");
        incomeDataset.setColor(ContextCompat.getColor(this.getActivity(), R.color.green_text));

        //-- Creates Expense Dataset with Entries
        BarDataSet expensesDataset = new BarDataSet(expensesEntries, "Despesa");
        expensesDataset.setColor(ContextCompat.getColor(this.getActivity(), R.color.red_text));

        //-- Creates label formatter
        final String[] monthLabels = {
                "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"
        };

        IAxisValueFormatter axisValueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (value < 0 || value > 22)
                    return Float.toString(value);

                int index = (int) Math.floor(value/2f);

                return monthLabels[index];

            }
        };


        //-- Combine Incomes and Expenses Dataset
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(incomeDataset);
        dataSets.add(expensesDataset);

        //-- Create Chart data with combined datasets
        BarData barData = new BarData(dataSets);

        //-- Format Chart xAxis
        XAxis xAxis = mMonthlyChart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(11f);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(axisValueFormatter);

        //-- Format yAxis
        YAxis leftAxis = mMonthlyChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        //-- Format chart legend
        Legend mLegend = mMonthlyChart.getLegend();
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        mLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        mLegend.setDrawInside(false);
        mLegend.setXEntrySpace(5);
        mLegend.setYEntrySpace(5);
        mLegend.setTextSize(15f);
        mLegend.setTextColor(Color.BLACK);
        mLegend.setFormSize(10f);
        mLegend.setWordWrapEnabled(true);

        //-- Config chart generic options
        mMonthlyChart.setEnabled(false);
        mMonthlyChart.setDescription(null);

        //-- Sets chart data
        mMonthlyChart.setData(barData);

        //-- Sets chart formatting
        mMonthlyChart.groupBars(0f, groupSpace, barSpace);
        mMonthlyChart.getXAxis().setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace) * 12);
        mMonthlyChart.setVisibleXRangeMaximum(12);
        mMonthlyChart.setDrawGridBackground(false);
        mMonthlyChart.setHorizontalScrollBarEnabled(true);
        mMonthlyChart.getAxisRight().setEnabled(false);

        //-- Updates chart view
        mMonthlyChart.invalidate();




        return view;
    }

    /*
    * ------------------------------------ Dummy Content -------------------------------------------
    */


    /**
     * Fills Dummy Budget Content
     */
    void fillDummyContent(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);

        final List<BudgetTransactionCategories> categories = new ArrayList<>();

        //Creates dummy categories
        for (int i = 0; i < 5; i++)
            categories.add(
                    new BudgetTransactionCategories(
                            Integer.toString(i),
                            Integer.toString(i) + " category"
                    )
            );



        //Create dummy revenues with random values
        Random rand = new Random();
        for (int i = 0; i < 12; i++){
            final int aux = i;
            calendar.add(Calendar.MONTH, i);
            BudgetTransaction budgetTransaction =
                    new BudgetTransaction(
                            "1",
                            System.currentTimeMillis(),
                            System.currentTimeMillis(),
                            calendar.getTimeInMillis(),
                            "aaaa",
                            new HashMap<String, BudgetTransactionCategories>()
                            {{
                                put(categories.get(aux%5).getId(), categories.get(aux%5));
                                put(categories.get(aux%5).getId(), categories.get(aux*2%5));
                                put(categories.get(aux%5).getId(), categories.get(aux*3%5));

                            }},
                            100000 + (10000 * rand.nextInt(5))
                    );
            incomes.add(budgetTransaction);
        }

        //Create dummy expenses with random values
        for (int i = 0; i < 12; i++){
            final int aux = i;
            calendar.add(Calendar.MONTH, i);
            BudgetTransaction budgetTransaction =
                    new BudgetTransaction(
                            "1",
                            System.currentTimeMillis(),
                            System.currentTimeMillis(),
                            calendar.getTimeInMillis(),
                            "aaaa",
                            new HashMap<String, BudgetTransactionCategories>()
                            {{
                                put(categories.get(aux%5).getId(), categories.get(aux%5));
                                put(categories.get(aux%5).getId(), categories.get(aux*2%5));
                                put(categories.get(aux%5).getId(), categories.get(aux*3%5));

                            }},
                            100000 + (10000 * rand.nextInt(5))
                    );
            expenses.add(budgetTransaction);
        }

    }





}
