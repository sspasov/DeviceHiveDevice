package com.devicehive.sspasov.device.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dataart.android.devicehive.Command;
import com.devicehive.sspasov.device.R;

import java.util.List;

public class DeviceCommandsFragment extends ListFragment {

    private final String TAG = DeviceCommandsFragment.class.getSimpleName();

    private static DeviceCommandsFragment instance;

    private List<Command> commands;
    private CommandsAdapter commandsAdapter;


    public static DeviceCommandsFragment getInstance() {
        if (instance == null) {
            instance = new DeviceCommandsFragment();
        }
        return instance;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
        if (getActivity() != null) {
            commandsAdapter = new CommandsAdapter(getActivity(), commands);
            commandsAdapter.notifyDataSetChanged();
            setListAdapter(commandsAdapter);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (commands != null) {
            commandsAdapter = new CommandsAdapter(activity, commands);
            setListAdapter(commandsAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(commands.get(position).toString());
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private static class CommandsAdapter extends BaseAdapter {

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
                convertView = inflater.inflate(R.layout.command_list_item, null);
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

}
