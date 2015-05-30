package com.devicehive.sspasov.device.fragments;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dataart.android.devicehive.EquipmentData;
import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.objects.EquipmentTypeConverter;

import java.util.List;

public class EquipmentListFragment extends ListFragment {

	private List<EquipmentData> equipment;
	private EquipmentAdapter equipmentAdapter;

    List<Sensor> deviceSensors;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
	}

	public static EquipmentListFragment newInstance() {
		return new EquipmentListFragment();
	}

	public void setEquipment(List<EquipmentData> equipment) {
		this.equipment = equipment;
		if (getActivity() != null) {
			equipmentAdapter = new EquipmentAdapter(getActivity(), this.equipment);
			setListAdapter(equipmentAdapter);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (equipment != null) {
			equipmentAdapter = new EquipmentAdapter(activity, equipment);
			setListAdapter(equipmentAdapter);
		}
	}

	private static class EquipmentAdapter extends BaseAdapter {

		private final LayoutInflater inflater;
		private List<EquipmentData> equipment;

		public EquipmentAdapter(Context context, List<EquipmentData> equipment) {
			this.equipment = equipment;
			this.inflater = LayoutInflater.from(context);
		}

		public void setEquipment(List<EquipmentData> equipment) {
			this.equipment = equipment;
			notifyDataSetChanged();
		}

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
				convertView = inflater.inflate(R.layout.equipment_list_item, null);
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
			holder.type.setText(EquipmentTypeConverter.toString(equipmentData.getType()));

			//holder.data.setText(equipmentData.getData() != null ? equipmentData.getData().toString() : "--");
			return convertView;
		}

		private class ViewHolder {
			TextView name;
			TextView code;
			TextView type;
			//TextView data;
		}

	}

}