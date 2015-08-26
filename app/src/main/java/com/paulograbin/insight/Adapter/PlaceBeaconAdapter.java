package com.paulograbin.insight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.PlaceBeacon;
import com.paulograbin.insight.R;

import java.util.List;

/**
 * Created by paulograbin on 15/08/15.
 */
public class PlaceBeaconAdapter extends ArrayAdapter<PlaceBeacon> {

    public PlaceBeaconAdapter(Context context, List<PlaceBeacon> paths) {
        super(context, android.R.layout.simple_list_item_1, paths);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_placebeacon, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlaceBeacon pb = getItem(position);
        PlaceProvider placeProvider = new PlaceProvider(getContext());
        BeaconProvider beaconProvider = new BeaconProvider(getContext());

        holder.idPlace.setText(pb.getId() + "");
        holder.placeName.setText(placeProvider.getByID(pb.getIdPlace()).getName());
        holder.placeId.setText(pb.getIdPlace() + "");
        holder.uuid.setText(pb.getUuid());

        return convertView;
    }

    public class ViewHolder {
        TextView idPlace;
        TextView placeName;
        TextView placeId;
        TextView uuid;

        public ViewHolder(View v) {
            idPlace = (TextView) v.findViewById(R.id.txtPlaceBeaconID);
            placeName = (TextView) v.findViewById(R.id.txtPlaceName);
            placeId = (TextView) v.findViewById(R.id.txtPlaceID);
            uuid = (TextView) v.findViewById(R.id.txtBeaconUUID);
        }
    }
}
