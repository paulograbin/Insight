package com.paulograbin.insight.Adapter;

import android.content.Context;
import android.location.Location;
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
 * Created by paulograbin on 22/08/15.
 */
public class PlaceAdapter extends ArrayAdapter<Place> {

    Location currentLocation;

    public PlaceAdapter(Context context, List<Place> places, Location currentLocation) {
        super(context, android.R.layout.simple_list_item_1, places);
        this.currentLocation = currentLocation;
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

        Location newLocation = new Location("teste");
        newLocation.setLatitude(p.getLatitude());
        newLocation.setLongitude(p.getLongitude());

        holder.txtPlaceName.setText(p.getName());
        holder.txtPlaceDistance.setText(getDistanceFromCurrentLocation(newLocation) + "");

        return convertView;
    }

    private float getDistanceFromCurrentLocation(Location newLocation) {
        return currentLocation.distanceTo(newLocation);
    }

    public class ViewHolder {
        TextView txtPlaceName;
        TextView txtPlaceDistance;

        public ViewHolder(View v) {
            txtPlaceName = (TextView) v.findViewById(R.id.txtPlaceName);
            txtPlaceDistance = (TextView) v.findViewById(R.id.txtPlaceDistance);
        }
    }
}
