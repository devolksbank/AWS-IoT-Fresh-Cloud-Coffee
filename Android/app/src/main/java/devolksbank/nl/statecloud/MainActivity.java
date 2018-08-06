package devolksbank.nl.statecloud;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import devolksbank.nl.statecloud.model.Device;
import devolksbank.nl.statecloud.services.CustomDynamoDBMapperConfig;
import devolksbank.nl.statecloud.services.listeners.GetDevicesCompleteListener;
import devolksbank.nl.statecloud.services.listeners.SetDeviceStatusCompleteListener;
import devolksbank.nl.statecloud.services.tasks.GetDevicesTask;

public class MainActivity extends AppCompatActivity implements GetDevicesCompleteListener, SetDeviceStatusCompleteListener {

    private DynamoDBMapper dynamoDBMapper;
    private AmazonDynamoDBClient dynamoDBClient;
    private ListView deviceListView;
    private final Context context = this;
    private SwipeRefreshLayout refreshLayout;
    private CustomDynamoDBMapperConfig dynamodbConfig;

    // View Lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get AWS dynamo configuration
        dynamodbConfig = new CustomDynamoDBMapperConfig(this);
        AWSMobileClient.getInstance().initialize(this).execute();

        // Set AWS dynamo configuration
        this.dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        dynamoDBClient.setRegion(dynamodbConfig.getRegion());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        deviceListView = findViewById(R.id.device_list_view);
        DeviceAdapter adapter = new DeviceAdapter(context, new ArrayList<Device>());
        deviceListView.setAdapter(adapter);
        deviceListView.setOnItemClickListener(new RowClickListener(getApplicationContext(), dynamoDBMapper, dynamodbConfig, this));

        refreshLayout = findViewById(R.id.swiperefresh);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetDevicesTask(dynamoDBMapper, dynamodbConfig, context, MainActivity.this).execute();
            }
        });

        new GetDevicesTask(dynamoDBMapper, dynamodbConfig, context, this).execute();

        // Get Firebase Token for receiving push notifications
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM/TOKEN", null == token ? "Could not get token" : token);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("awsPush"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    // Shows dialog when push notification is received and app is open
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String body = intent.getStringExtra("data");

            new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert)
                    .setTitle("Device Status has been changed")
                    .setMessage(body)
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();
        }
    };

    @Override
    public void getDevicesComplete(ArrayList<Device> devices) {
        DeviceAdapter adapter = (DeviceAdapter) deviceListView.getAdapter();
        adapter.setDataSource(devices);
        adapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setDeviceStatusComplete() {
        new GetDevicesTask(dynamoDBMapper, dynamodbConfig, context, MainActivity.this).execute();
    }

    //Adds about menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Makes about item clickable
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, DisplayAboutPageActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
