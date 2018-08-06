package devolksbank.nl.statecloud.services.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.DeviceStatusDO;

import devolksbank.nl.statecloud.model.DeviceStatus;
import devolksbank.nl.statecloud.services.CustomDynamoDBMapperConfig;
import devolksbank.nl.statecloud.services.listeners.SetDeviceStatusCompleteListener;

/**
 * Created by casper on 6/8/18.
 */

public class SetDeviceStatusTask extends AsyncTask<Integer,Integer,Object> {
    DynamoDBMapper dynamoDBMapper;
    CustomDynamoDBMapperConfig dynamoDBConfig;
    Context context;
    SetDeviceStatusCompleteListener listener;

    public SetDeviceStatusTask(DynamoDBMapper dynamoDBMapper, CustomDynamoDBMapperConfig dynamoDBConfig, Context context, SetDeviceStatusCompleteListener listener) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBConfig = dynamoDBConfig;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Integer[] deviceIds) {
        final DeviceStatusDO device = new DeviceStatusDO();
        device.setDeviceID((double) deviceIds[0]);
        device.setStatus(DeviceStatus.WORKING.toString());
        device.setTimestamp((double) System.currentTimeMillis());

        dynamoDBMapper.save(device, dynamoDBConfig.getDeviceStatusConfig());
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Do something with progressBar or spinner

    }

    @Override
    protected void onPostExecute(Object o) {
        // TODO: Return saved object so it's not necessary update all the devices
        listener.setDeviceStatusComplete();
    }
}
