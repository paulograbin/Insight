package com.paulograbin.insight.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paulograbin.insight.R;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by paulograbin on 22/07/15.
 */
public class NearBeaconListAdapter extends ArrayAdapter<Beacon> {

    public NearBeaconListAdapter(Context context, ArrayList<Beacon> beacons) {
        super(context, android.R.layout.simple_list_item_1, beacons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item_beacon, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Beacon beacon = getItem(position);

        holder.txtUuid.setText(beacon.getId1().toString());
        holder.txtMajor.setText(beacon.getId2().toString());
        holder.txtMinor.setText(beacon.getId3().toString());

        return convertView;
    }

    public class ViewHolder {
        TextView txtUuid;
        TextView txtMajor;
        TextView txtMinor;

        public ViewHolder(View v) {
            txtUuid = (TextView) v.findViewById(R.id.txtUUID);
            txtMajor = (TextView) v.findViewById(R.id.txtMajor);
            txtMinor = (TextView) v.findViewById(R.id.txtMinor);
        }
    }
}
