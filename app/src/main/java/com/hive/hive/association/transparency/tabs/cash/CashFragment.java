package com.hive.hive.association.transparency.tabs.cash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hive.hive.R;
import com.hive.hive.model.association.BudgetTransaction;
import com.hive.hive.model.association.BudgetTransactionCategories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class CashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PAGE = "Caixa";

    private List<BudgetTransaction> transactions;



    public CashFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CashFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CashFragment fragment = new CashFragment();
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
        return inflater.inflate(R.layout.fragment_cash, container, false);
    }

    /*
    * ---------------- Dummy Content
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
