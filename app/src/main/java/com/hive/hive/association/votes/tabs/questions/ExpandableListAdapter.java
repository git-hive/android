package com.hive.hive.association.votes.tabs.questions;

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
import com.hive.hive.association.transparency.tabs.staff.CustomGridView;
import com.hive.hive.association.transparency.tabs.staff.GridAdapter;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

	private Context _context;
	private List<String> header; // header titles
	// Child data in format of header title, child title
	private HashMap<String, List<String>> child;

	static final String[] MOBILE_OS = new String[] { "Android", "iOS",
			"Windows", "Blackberry" };

	public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
		this._context = context;
		this.header = listDataHeader;
		this.child = listChildData;
	}

	@Override
	public int getGroupCount() {
		// Get header size
		return this.header.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// return children count
		return this.child.get(this.header.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// Get header position
		return this.header.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// This will return the child
		return this.child.get(this.header.get(groupPosition)).get(childPosition);
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		// Getting header title
		String headerTitle = (String) getGroup(groupPosition);

		// Inflating header layout and setting text
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.question_header, parent, false);
		}

		TextView header_text = (TextView) convertView.findViewById(R.id.header);
		header_text.setText(headerTitle);

		// If group is expanded then change the text into bold and change the
		// icon
		if (isExpanded) {
			header_text.setTypeface(null, Typeface.BOLD);
			header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0);
		} else {
			// If group is not expanded then change the text back into normal
			// and change the icon

			header_text.setTypeface(null, Typeface.NORMAL);
			header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0);
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// Getting child text
		final String childText = (String) getChild(groupPosition, childPosition);
		// Inflating child layout and setting textview
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//convertView = infalInflater.inflate(R.layout.question_child, parent, false);
			convertView = infalInflater.inflate(R.layout.question_grid_view, null);
		}
		CustomGridView gridView = (CustomGridView) convertView
				.findViewById(R.id.questionGV);

		gridView.setNumColumns(2);// gridView.setGravity(Gravity.CENTER);//
		gridView.setHorizontalSpacing(10);// SimpleAdapter adapter =
		GridAdapter adapter = new GridAdapter(this._context, MOBILE_OS);
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

		TextView child_text = (TextView) convertView.findViewById(R.id.child);

		child_text.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}