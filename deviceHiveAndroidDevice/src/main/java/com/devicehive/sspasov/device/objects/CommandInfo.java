package com.devicehive.sspasov.device.objects;

import com.dataart.android.devicehive.Command;
import com.dataart.android.devicehive.device.CommandResult;

import java.util.HashMap;

/**
 * Created by toni on 29.05.15.
 */
public class CommandInfo {

    /**Whole command package*/
    private Command command;

    /**Command name is the input name witch describes command */
    private String name;

    /**Parameters in the original command */
    private HashMap inputParams;

    /**Which equipment/device is dedicated to */
    private String type;

    /**Status of the command Completed/Failed */
    private String status;

    /**Result string, human reading output for the user */
    private String result;

    /**Parameters passed in the response */
    private HashMap outputParams;


    public CommandInfo(Command command) {
        this.command = command;
       fillFields();
    }

    private void fillFields() {
        name = command.getCommand();
        inputParams = (HashMap) command.getParameters();
        status = CommandResult.STATUS_COMLETED;
        outputParams = new HashMap();
    }

    public HashMap getInputParams() {
        return inputParams;
    }

    public void setInputParams(HashMap inputParams) {
        this.inputParams = inputParams;
    }

    public HashMap getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(HashMap outputParams) {
        this.outputParams = outputParams;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
