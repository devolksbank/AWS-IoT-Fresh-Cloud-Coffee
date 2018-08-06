package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "READ_FROM_CONFIG")

public class DeviceTypeDO {
    private Double _deviceID;
    private String _deviceType;

    @DynamoDBHashKey(attributeName = "deviceID")
    @DynamoDBAttribute(attributeName = "deviceID")
    public Double getDeviceID() {
        return _deviceID;
    }

    public void setDeviceID(final Double _deviceID) {
        this._deviceID = _deviceID;
    }

    @DynamoDBAttribute(attributeName = "deviceType")
    public String getDeviceType() {
        return _deviceType;
    }

    public void setDeviceType(final String _deviceType) {
        this._deviceType = _deviceType;
    }

}
