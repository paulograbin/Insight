package com.paulograbin.insight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.R;

import java.util.List;

/**
 * Created by paulograbin on 15/08/15.
 */
public class PathListAdapter extends ArrayAdapter<Path> {

    public PathListAdapter(Context context, List<Path> paths) {
        super(context, android.R.layout.simple_list_item_1, paths);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_path_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Path p = getItem(position);

        PlaceProvider pp = new PlaceProvider(super.getContext());

        holder.pathPlaceFrom.setText(pp.getByID(p.getPlace()).getName());
        holder.pathPlaceTo.setText(pp.getByID(p.getConnectedTo()).getName());
        holder.pathWeight.setText(p.getWeight() + "");

        return convertView;
    }

    public class ViewHolder {
        TextView pathPlaceFrom;
        TextView pathPlaceTo;
        TextView pathWeight;

        public ViewHolder(View v) {
            pathPlaceFrom = (TextView) v.findViewById(R.id.txtPlaceFrom);
            pathPlaceTo = (TextView) v.findViewById(R.id.txtPlaceTo);
            pathWeight = (TextView) v.findViewById(R.id.txtWeight);
        }
    }
}
