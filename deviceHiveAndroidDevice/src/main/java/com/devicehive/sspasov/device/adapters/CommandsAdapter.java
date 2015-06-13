package com.devicehive.sspasov.device.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dataart.android.devicehive.Command;
import com.devicehive.sspasov.device.R;

import java.util.List;

/**
 * Created by toni on 13.06.15.
 */
public class CommandsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Command> commands;

    public CommandsAdapter(Context context, List<Command> commands) {
        this.commands = commands;
        this.inflater = LayoutInflater.from(context);
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return commands.size();
    }

    @Override
    public Object getItem(int position) {
        return commands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_command, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_command_name);
            holder.params = (TextView) convertView.findViewById(R.id.tv_command_params);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Command command = commands.get(position);
        holder.name.setText(command.getId() + ". " + command.getCommand());
        holder.params.setText(command.getParameters().toString());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView params;
    }

}