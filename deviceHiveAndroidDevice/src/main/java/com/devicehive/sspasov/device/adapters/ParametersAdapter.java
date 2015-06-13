package com.devicehive.sspasov.device.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devicehive.sspasov.device.R;
import com.devicehive.sspasov.device.objects.Parameter;

import java.util.List;

/**
 * Created by toni on 13.06.15.
 */
public class ParametersAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Parameter> parameters;

    public ParametersAdapter(Context context, List<Parameter> parameters) {
        this.parameters = parameters;
        this.inflater = LayoutInflater.from(context);
    }

    public void removeParameter(int position) {
        parameters.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return parameters.size();
    }

    @Override
    public Object getItem(int position) {
        return parameters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_parameters, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.iv_add_parameter);
            holder.name = (TextView) convertView.findViewById(R.id.tv_parameter_name);
            holder.value = (TextView) convertView.findViewById(R.id.tv_parameter_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Parameter parameter = parameters.get(position);
        if (position == 0) {
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(parameter.name);
        holder.value.setText(parameter.value);
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView name;
        TextView value;
    }
}