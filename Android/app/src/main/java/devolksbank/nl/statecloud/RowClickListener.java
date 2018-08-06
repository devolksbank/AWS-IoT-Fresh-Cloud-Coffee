package devolksbank.nl.statecloud;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.DeviceStatusDO;

import devolksbank.nl.statecloud.model.Device;
import devolksbank.nl.statecloud.model.DeviceStatus;
import devolksbank.nl.statecloud.model.DeviceType;
import devolksbank.nl.statecloud.services.CustomDynamoDBMapperConfig;
import devolksbank.nl.statecloud.services.listeners.SetDeviceStatusCompleteListener;
import devolksbank.nl.statecloud.services.tasks.SetDeviceStatusTask;


/**
 * Click Listener for listview rows.
 * Adds a dialog to submit devices as fixed.
 */
public class RowClickListener implements AdapterView.OnItemClickListener {

    private final DynamoDBMapper dynamoDBMapper;
    private final Context context;
    private final CustomDynamoDBMapperConfig dynamodbConfig;
    private final SetDeviceStatusCompleteListener deviceStatusCompleteListener;

    public RowClickListener(Context context, DynamoDBMapper dynamoDBMapper, CustomDynamoDBMapperConfig dynamodbConfig, SetDeviceStatusCompleteListener listener) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.context = context;
        this.dynamodbConfig = dynamodbConfig;
        this.deviceStatusCompleteListener = listener;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
        final Device device = (Device) adapterView.getItemAtPosition(i);

        if (DeviceStatus.DEFECT != device.getStatus()) {
            return;
        }

        // Create dialog for submitting fixed device
        final AlertDialog alertDialog = new AlertDialog.Builder(adapterView.getContext()).create();
        alertDialog.setTitle(R.string.fix_confirmation_header);
        alertDialog.setMessage(context.getResources().getString(R.string.fix_confirmation, DeviceType.toString(context, device.type), device.deviceId));

        // Add No button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.fix_confirmation_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        // Add Yes button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.fix_confirmation_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new SetDeviceStatusTask(dynamoDBMapper,dynamodbConfig, context, deviceStatusCompleteListener).execute(device.deviceId);
                alertDialog.dismiss();
                Toast.makeText(adapterView.getContext(), context.getResources().getString(R.string.submit_fix_to_database), Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}
