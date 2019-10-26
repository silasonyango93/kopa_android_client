package com.silas.digitalfactory.kopa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter2 extends ArrayAdapter<EmploymentCategoriesModel> {

    //the list values in the List of type hero
    List<EmploymentCategoriesModel> heroList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public MyListAdapter2(Context context, int resource, List<EmploymentCategoriesModel> heroList) {
        super(context, resource, heroList);
        this.context = context;
        this.resource = resource;
        this.heroList = heroList;

    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);

        //getting the hero of the specified position
        EmploymentCategoriesModel hero = heroList.get(position);

        //adding values to the list item
        textViewName.setText(hero.getCategoryDescription());


        //adding a click listener to the button to remove item from the list

        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //removeHero(position);
            }
        });




        //finally returning the view
        return view;
    }




}