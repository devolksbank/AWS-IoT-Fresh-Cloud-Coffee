package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "READ_FROM_CONFIG")

public class DeviceStatusDO {
    private Double _deviceID;
    private Double _timestamp;
    private String _status;

    @DynamoDBHashKey(attributeName = "deviceID")
    @DynamoDBAttribute(attributeName = "deviceID")
    public Double getDeviceID() {
        return _deviceID;
    }

    public void setDeviceID(final Double _deviceID) {
        this._deviceID = _deviceID;
    }
    @DynamoDBRangeKey(attributeName = "timestamp")
    @DynamoDBAttribute(attributeName = "timestamp")
    public Double getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(final Double _timestamp) {
        this._timestamp = _timestamp;
    }
    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return _status;
    }

    public void setStatus(final String _status) {
        this._status = _status;
    }

}
