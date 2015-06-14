package com.devicehive.sspasov.device.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dataart.android.devicehive.EquipmentData;
import com.devicehive.sspasov.device.R;

import java.util.List;

/**
 * Created by toni on 13.06.15.
 */
public class EquipmentAdapter extends BaseAdapter {
    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private final LayoutInflater inflater;
    private List<EquipmentData> equipment;

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public EquipmentAdapter(Context context, List<EquipmentData> equipment) {
        this.equipment = equipment;
        this.inflater = LayoutInflater.from(context);
    }

    public void setEquipment(List<EquipmentData> equipment) {
        this.equipment = equipment;
        notifyDataSetChanged();
    }

    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public int getCount() {
        return equipment.size();
    }

    @Override
    public Object getItem(int position) {
        return equipment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_equipment, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.equipment_name_text_view);
            holder.code = (TextView) convertView.findViewById(R.id.equipment_code_text_view);
            holder.type = (TextView) convertView.findViewById(R.id.tv_equipment_type);
            //holder.data = (TextView) convertView.findViewById(R.id.equipment_data_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EquipmentData equipmentData = equipment.get(position);
        holder.name.setText(equipmentData.getName());
        holder.code.setText(equipmentData.getCode());
        holder.type.setText(equipmentData.getType());
        //holder.data.setText(equipmentData.getData() != null ? equipmentData.getData().toString() : "--");

        return convertView;
    }

    // ---------------------------------------------------------------------------------------------
    // Inner classes
    // ---------------------------------------------------------------------------------------------
    private class ViewHolder {
        TextView name;
        TextView code;
        TextView type;
        //TextView data;
    }

}
