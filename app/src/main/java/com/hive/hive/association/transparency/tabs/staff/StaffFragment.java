package com.hive.hive.association.transparency.tabs.staff;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

import com.hive.hive.R;
import com.hive.hive.association.transparency.TransparencyActivity;

import java.util.ArrayList;


public class StaffFragment extends Fragment {

    private static final String TAG = StaffFragment.class.getSimpleName();
    public static final String ARG_PAGE = "Funcionários";
    private int mColumnCount = 3;

    private OnListFragmentInteractionListener mListener;
    private ArrayList<Staff> mStaffMembers;
    private ArrayList<StaffAdapter> mAdapters;


    private ArrayList<Staff> mStaffAdmin;
    private ArrayList<Staff> mStaffGardeners;
    private ArrayList<Staff> mStaffCleaning;
    private ArrayList<Staff> mStaffSecurity;
    private ArrayList<Staff> mStaffServices;

    //-- Context
    private TransparencyActivity mActivity;

    public StaffFragment() {
        // Required empty public constructor
    }

    public static StaffFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StaffFragment fragment = new StaffFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapters = new ArrayList<>();
        mStaffMembers = new ArrayList<>();
        mStaffAdmin = new ArrayList<>();
        mStaffGardeners = new ArrayList<>();
        mStaffCleaning = new ArrayList<>();
        mStaffSecurity = new ArrayList<>();
        mStaffServices = new ArrayList<>();
        prepareMockData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_staff, container, false);

        mAdapters.add(setAdapters(view, R.id.staff_group_1_RV, mStaffAdmin));
        mAdapters.add(setAdapters(view, R.id.staff_group_2_RV, mStaffGardeners));
        mAdapters.add(setAdapters(view, R.id.staff_group_3_RV, mStaffCleaning));
//        mAdapters.add(setAdapters(view, R.id.staff_group_4_RV, mStaffMembers));
        mAdapters.add(setAdapters(view, R.id.staff_group_5_RV, mStaffSecurity));
        mAdapters.add(setAdapters(view, R.id.staff_group_6_RV, mStaffServices));


        //--- Group Views
        LinearLayout linearLayout = view.findViewById(R.id.staff_group_1_LL);
        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_1_RV, R.id.staff_group_1_arrow_iv));

        linearLayout = view.findViewById(R.id.staff_group_2_LL);
        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_2_RV, R.id.staff_group_2_arrow_iv));

        linearLayout = view.findViewById(R.id.staff_group_3_LL);
        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_3_RV, R.id.staff_group_3_arrow_iv));
//
//        linearLayout = view.findViewById(R.id.staff_group_4_LL);
//        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_4_RV, R.id.staff_group_4_arrow_iv));

        linearLayout = view.findViewById(R.id.staff_group_5_LL);
        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_5_RV, R.id.staff_group_5_arrow_iv));

        linearLayout = view.findViewById(R.id.staff_group_6_LL);
        linearLayout.setOnClickListener(getGroupOnClickListener(R.id.staff_group_6_RV, R.id.staff_group_6_arrow_iv));

        mActivity = (TransparencyActivity) getActivity();

        return  view;
    }

    private StaffAdapter setAdapters(View baseView, int recycleViewId, ArrayList<Staff> staff){
        RecyclerView recyclerView = baseView.findViewById(recycleViewId);

        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(baseView.getContext(), LinearLayoutManager.VERTICAL, false));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(baseView.getContext(), mColumnCount));

        StaffAdapter staffAdapter = new StaffAdapter(staff, mListener);
        recyclerView.setAdapter(staffAdapter);

        return staffAdapter;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Staff item);
    }

    /*
         * Preparing the list data
         */

    public class Staff {

        public String name;
        public int imageId;

        public Staff(String name, int imageId){
            this.name = name;
            this.imageId = imageId;
        }

    }

    void prepareMockData(){
        mStaffMembers.add(new Staff("C3PO", R.drawable.avatar_c3po));
        mStaffMembers.add(new Staff("Jedi Linux",  R.drawable.avatar_linux));
        mStaffMembers.add(new Staff("Chewbaca",  R.drawable.avatar_chewie));
        mStaffMembers.add(new Staff("R2D2",  R.drawable.avatar_r2d2));
        mStaffMembers.add(new Staff("Darth Maul",  R.drawable.avatar_maul));
        mStaffMembers.add(new Staff("Darth Vader",  R.drawable.avatar_vader));
        mStaffMembers.add(new Staff("Master Yoda",  R.drawable.avatar_yoda));
        mStaffMembers.add(new Staff("Princess Leia",  R.drawable.avatar_leia));
        mStaffMembers.add(new Staff("Stormtrooper",  R.drawable.avatar_trooper));

        //--- Admin
        mStaffAdmin.add(new Staff("Diretor 1", R.drawable.diretor2));
        mStaffAdmin.add(new Staff("Diretor 2",  R.drawable.diretor3));
        mStaffAdmin.add(new Staff("Diretor 3",  R.drawable.diretor4));
        //mStaffAdmin.add(new Staff("Diretor 4",  R.drawable.diretor5));

        //--- Gardening
        mStaffGardeners.add(new Staff("Jardineiro 1", R.drawable.gardener1));
        mStaffGardeners.add(new Staff("Jardineiro 2",  R.drawable.gardener2));
        mStaffGardeners.add(new Staff("Jardineiro 3",  R.drawable.gardener3));

        //--- Cleaning
        mStaffCleaning.add(new Staff("Zelador 1", R.drawable.janitor1));
        mStaffCleaning.add(new Staff("Zelador 2",  R.drawable.janitor2));
        mStaffCleaning.add(new Staff("Zelador 3",  R.drawable.janitor3));

        //--- Security
        mStaffSecurity.add(new Staff("Segurança 1", R.drawable.safety1));
        mStaffSecurity.add(new Staff("Segurança 2",  R.drawable.safety2));
        mStaffSecurity.add(new Staff("Segurança 3",  R.drawable.safety3));
        mStaffSecurity.add(new Staff("Segurança 4", R.drawable.safety4));
        mStaffSecurity.add(new Staff("Segurança 5",  R.drawable.safety5));
        mStaffSecurity.add(new Staff("Segurança 6",  R.drawable.safety6));


        //--- Services
        mStaffServices.add(new Staff("Serviços 1", R.drawable.service1));
        mStaffServices.add(new Staff("Serviços 2",  R.drawable.service2));
        mStaffServices.add(new Staff("Serviços 3",  R.drawable.service3));
        //mStaffServices.add(new Staff("Serviços 4",  R.drawable.service4));


    }

    private View.OnClickListener getGroupOnClickListener(final int targetViewId, final int arrowId){
        final Activity activity = getActivity();
        if (activity == null){
            Log.d(TAG, "Activity null");
            return null;
        }


        return (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View rootView = activity.findViewById(targetViewId);
                if (rootView != null){


                    final ImageView arrowIV = rootView.getRootView().findViewById(arrowId);

                    if (rootView.getVisibility() == View.VISIBLE) {

                        //Arrow
                        if (arrowIV != null){
                            arrowIV.clearAnimation();
                            Log.d(TAG, "Rotating arrow");
                            AnimationSet animSet = new AnimationSet(true);
                            animSet.setInterpolator(new DecelerateInterpolator());
                            animSet.setFillAfter(true);
                            animSet.setFillEnabled(true);

                            final RotateAnimation animRotate = new RotateAnimation(180.0f, 0.0f,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                            animRotate.setDuration(250);
                            animRotate.setFillAfter(true);
                            animSet.addAnimation(animRotate);

                            arrowIV.startAnimation(animSet);
                        }
                        else{
                            Log.d(TAG, "Error in rotating arrow");
                        }


                        //Recycle
                        rootView.clearAnimation();
                        rootView.animate().alpha(0.0f)
                                .setDuration(250)
                                .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                rootView.clearAnimation();
                                rootView.setVisibility(View.GONE);
                                Log.d(TAG, "Hide Animation Completed");
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                                rootView.clearAnimation();
                                Log.d(TAG, "Hide Animation Canceled");
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        }).start();
                    }
                    else {


                        //Arrow
                        if (arrowIV != null){
                            arrowIV.clearAnimation();
                            Log.d(TAG, "Rotating arrow");
                            AnimationSet animSet = new AnimationSet(true);
                            animSet.setInterpolator(new DecelerateInterpolator());
                            animSet.setFillAfter(true);
                            animSet.setFillEnabled(true);

                            final RotateAnimation animRotate = new RotateAnimation(0.0f, 180.0f,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                            animRotate.setDuration(250);
                            animRotate.setFillAfter(true);
                            animSet.addAnimation(animRotate);

                            arrowIV.startAnimation(animSet);
                        }
                        else{
                            Log.d(TAG, "Error in rotating arrow");
                        }

                        //Recycle
                        rootView.animate()
                                .alpha(1.0f)
                                .setDuration(250)
                                .setListener(new Animator.AnimatorListener() {

                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        rootView.clearAnimation();
                                        rootView.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Show Animation Completed");
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {
                                        rootView.clearAnimation();
                                        Log.d(TAG, "Show Animation Canceled");
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                }).start();
                    }
                }
                else
                    Log.d(TAG, "Group View not Found 2");
            }
        });
    }


}
