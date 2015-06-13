package com.devicehive.sspasov.device.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.dataart.android.devicehive.Command;
import com.devicehive.sspasov.device.adapters.CommandsAdapter;

import java.util.List;

public class DeviceCommandsFragment extends ListFragment {
    // ---------------------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------------------
    private final String TAG = DeviceCommandsFragment.class.getSimpleName();

    // ---------------------------------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------------------------------
    private List<Command> commands;
    private CommandsAdapter commandsAdapter;

    private static DeviceCommandsFragment instance;

    // ---------------------------------------------------------------------------------------------
    // Fragment life cycle
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (commands != null) {
            commandsAdapter = new CommandsAdapter(activity, commands);
            setListAdapter(commandsAdapter);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Public methods
    // ---------------------------------------------------------------------------------------------
    public static DeviceCommandsFragment getInstance() {
        if (instance == null) {
            instance = new DeviceCommandsFragment();
        }
        return instance;
    }

    public static DeviceCommandsFragment newInstance() {
        instance = new DeviceCommandsFragment();
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

    // ---------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // Override methods
    // ---------------------------------------------------------------------------------------------
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(commands.get(position).toString());
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
