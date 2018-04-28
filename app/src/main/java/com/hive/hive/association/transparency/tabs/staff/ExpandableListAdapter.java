package com.hive.hive.association.transparency.tabs.staff;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hive.hive.R;

/**
 * Created by birck on 16/02/18.
 */



public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> mListDataChild;
    private LayoutInflater mInflater;

    static final String[] MOBILE_OS = new String[] { "Android", "iOS",
            "Windows", "Blackberry" };

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        mContext = context;
        mListDataHeader = listDataHeader;
        mListDataChild = listChildData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = mInflater
                    .inflate(R.layout.list_item_staff_group, null);
        }
        TextView headerLabel = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        headerLabel.setTypeface(null, Typeface.BOLD);
        headerLabel.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.staff_grid_view, null);
        }
        CustomGridView gridView = (CustomGridView) convertView
                .findViewById(R.id.GridView_toolbar);

        gridView.setNumColumns(3);// gridView.setGravity(Gravity.CENTER);//
        gridView.setHorizontalSpacing(10);// SimpleAdapter mExpandableQuestionsAdapter =
        GridAdapter adapter = new GridAdapter(mContext, MOBILE_OS);
        gridView.setAdapter(adapter);// Adapter

        int totalHeight = 0;
        for (int size = 0; size < adapter.getCount(); size++) {
            RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
                    size, null, gridView);
            ImageView imageView = (ImageView) relativeLayout.getChildAt(0);
            imageView.measure(0, 0);
            totalHeight += imageView.getMeasuredHeight();
        }
        gridView.SetHeight(totalHeight);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}