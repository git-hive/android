package com.hive.hive.association.transparency.tabs.overview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hive.hive.R;
import com.hive.hive.model.association.BudgetTransaction;
import com.hive.hive.model.association.BudgetTransactionCategories;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "Caixa";

    //--- Context
    private Activity mActivity;

    //--- Data
    private List<BudgetTransaction> transactions;


    //--- Chart
    private BarChart monthlyChart;



    public OverviewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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

        transactions = new ArrayList<>();
        fillDummyContent();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //--- Activity and Context
        mActivity = getActivity();

        //--- Views
        monthlyChart = view.findViewById(R.id.monthly_chart);

        //--- Chart Data
        List<BarEntry> monthlyBudgets = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for(BudgetTransaction transaction : transactions){
            calendar.setTimeInMillis(transaction.getDate());
            monthlyBudgets.add(new BarEntry(calendar.get(Calendar.MONTH), transaction.getValue()));
        }

        //--- Chart Formatting
        BarDataSet barDataSet = new BarDataSet(monthlyBudgets, "Orçamento Mensal");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        dataSets.add(barDataSet);
        BarData barData = new BarData(dataSets);

//        //Format Axis
//        XAxis xAxis = monthlyChart.getXAxis();
//        YAxis yAxis = monthlyChart.getAxisLeft();
//
//        xAxis.setAxisMinimum(0);
//        xAxis.setAxisMaximum(12);
//        xAxis.setGranularity(0.5f);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setDrawGridLines(false);
//
//        yAxis.setDrawGridLines(false);
//        yAxis.setAxisMinimum(0.0f);
//        yAxis.setAxisMaximum(50000f);

        monthlyChart.setData(barData);
//        monthlyChart.groupBars(0f, 0.2f, 0.03f);
        monthlyChart.invalidate();



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



        //Create dummy transactions with random values
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
                            rand.nextFloat()
                    );
            transactions.add(budgetTransaction);
        }

    }





}
