package devolksbank.nl.statecloud.services.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.models.nosql.DeviceStatusDO;
import com.amazonaws.models.nosql.DeviceTypeDO;

import java.util.ArrayList;
import java.util.List;

import devolksbank.nl.statecloud.model.Device;
import devolksbank.nl.statecloud.model.DeviceStatus;
import devolksbank.nl.statecloud.model.DeviceType;
import devolksbank.nl.statecloud.services.CustomDynamoDBMapperConfig;
import devolksbank.nl.statecloud.services.listeners.GetDevicesCompleteListener;

/**
 * Created by casper on 6/8/18.
 */

public class GetDevicesTask extends AsyncTask<String,Integer,ArrayList<Device>> {
    DynamoDBMapper dynamoDBMapper;
    CustomDynamoDBMapperConfig dynamoDBConfig;
    Context context;
    GetDevicesCompleteListener listener;

    public GetDevicesTask(DynamoDBMapper dynamoDBMapper, CustomDynamoDBMapperConfig dynamoDBConfig, Context context, GetDevicesCompleteListener listener) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBConfig = dynamoDBConfig;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected ArrayList<Device> doInBackground(String... strings) {
        ArrayList<Device> devices = new ArrayList<>();

        DynamoDBScanExpression exp = new DynamoDBScanExpression();
        List<DeviceTypeDO> result = dynamoDBMapper.scan(DeviceTypeDO.class, exp, dynamoDBConfig.getDeviceTypeConfig());

        for (DeviceTypeDO deviceTypesDO : result) {
            Log.i("GetDevicesTask", "Fetched deviceType: " + deviceTypesDO);
            Device device = getDevice(deviceTypesDO.getDeviceID().intValue(), deviceTypesDO.getDeviceType());
            Log.i("GetDevicesTask", "Fetched device: " + device);
            devices.add(device);
        }

        return devices;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Do something with progressBar or spinner
    }

    @Override
    protected void onPostExecute(ArrayList<Device> devices) {
        listener.getDevicesComplete(devices);
    }

    private Device getDevice(double deviceId, String deviceType) {
        DeviceStatusDO deviceStatusDO = new DeviceStatusDO();
        deviceStatusDO.setDeviceID(deviceId);

        DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(deviceStatusDO)
                .withScanIndexForward(false)
                .withConsistentRead(false);
        PaginatedList<DeviceStatusDO> result = dynamoDBMapper.query(DeviceStatusDO.class, queryExpression, dynamoDBConfig.getDeviceStatusConfig());

        if (result.isEmpty()) {
            return  null;
        }

        deviceStatusDO = result.get(0);

        return new Device(DeviceType.fromString(context, deviceType), deviceStatusDO.getDeviceID().intValue(), DeviceStatus.fromString(context, deviceStatusDO.getStatus()), deviceStatusDO.getTimestamp());
    }

}
