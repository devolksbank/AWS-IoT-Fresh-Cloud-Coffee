package devolksbank.nl.statecloud.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.regions.Region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class for providing dynamodb settings from config
 */
public class CustomDynamoDBMapperConfig {

    public static final String PROPERTIES_FILE_NAME = "app.properties";
    public static final String DEVICE_STATUS_TABLE_PROPERTY_KEY = "dynamodb.device.status.table";
    public static final String DEVICE_TYPE_TABLE_PROPERTY_KEY = "dynamodb.device.type.table";
    public static final String DYNAMO_DB_REGION_KEY = "dynamodb.region";

    private final Properties props = new Properties();

    private final Context context;

    public CustomDynamoDBMapperConfig(final Context context) {
        this.context = context;
    }

    private void readConfigWhenNecessary() {
        if (!props.isEmpty()) {
            Log.d("CustomDBMapper", "No need to re-read the config properties");
            return;
        }

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(PROPERTIES_FILE_NAME);
            props.load(is);
        } catch (IOException ex) {
            Log.e("CustomDBMapper", "Unable to load app properties: " + ex.getLocalizedMessage());
        }
    }

    public Region getRegion() {
        readConfigWhenNecessary();
        String region = props.getProperty(DYNAMO_DB_REGION_KEY);
        Log.d("CustomDBMapper", "getRegion: region: " + region);
        return Region.getRegion(region);
    }

    public DynamoDBMapperConfig getDeviceStatusConfig() {
        readConfigWhenNecessary();
        String tableName = props.getProperty(DEVICE_STATUS_TABLE_PROPERTY_KEY);
        Log.d("CustomDBMapper", "getDeviceStatusConfig: table: " + tableName);
        return new DynamoDBMapperConfig.Builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName)).build();
    }

    public DynamoDBMapperConfig getDeviceTypeConfig() {
        readConfigWhenNecessary();
        String tableName = props.getProperty(DEVICE_TYPE_TABLE_PROPERTY_KEY);
        Log.d("CustomDBMapper", "getDeviceTypeConfig: table: " + tableName);
        return new DynamoDBMapperConfig.Builder().withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(tableName)).build();
    }
}
