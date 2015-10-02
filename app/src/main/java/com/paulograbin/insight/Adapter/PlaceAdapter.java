package com.paulograbin.insight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.R;

import java.util.List;

/**
 * Created by paulograbin on 15/08/15.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {

    public PlaceAdapter(Context context, List<Place> places) {
        super(context, android.R.layout.simple_list_item_1, places);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_place, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Place p = getItem(position);
        PlaceProvider pp = new PlaceProvider(super.getContext());

        holder.txtPlaceName.setText(p.getId() + " - " + p.getName());
        holder.txtPlaceDescription.setText(p.getDescription() + " / " + p.getMessage());

        return convertView;
    }

    public class ViewHolder {
        TextView txtPlaceName;
        TextView txtPlaceDescription;

        public ViewHolder(View v) {
            txtPlaceName = (TextView) v.findViewById(R.id.txtPlaceName);
            txtPlaceDescription = (TextView) v.findViewById(R.id.txtPlaceDescription);
        }
    }
}
