package com.softmed.tanzania.referral;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTwo extends Fragment {


    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        // Inflate the layout for this fragment

        PieChart pieChart = (PieChart) v.findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));



        PieDataSet dataset = new PieDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Successful Referrals");
        labels.add("Pending Referrals");



        PieData data = new PieData(labels, dataset);
        pieChart.setData(data);
        pieChart.setDescription("A pie chart showing both successful and pending referrals");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateY(5000);
        return v;
    }

}
