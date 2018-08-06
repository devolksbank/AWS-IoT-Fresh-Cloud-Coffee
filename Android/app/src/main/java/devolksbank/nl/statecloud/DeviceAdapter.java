package devolksbank.nl.statecloud;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import devolksbank.nl.statecloud.model.Device;
import devolksbank.nl.statecloud.model.DeviceStatus;
import devolksbank.nl.statecloud.model.DeviceType;

/**
 * Adapter for populating {@code listview_row}s with {@link Device} information.
 */
public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Device> dataSource;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");

    public DeviceAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.dataSource = devices;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataSource(ArrayList<Device> devices) {
        this.dataSource = devices;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.listview_row, viewGroup, false);

        TextView idTextField = rowView.findViewById(R.id.idTextView);
        TextView statusTextField = rowView.findViewById(R.id.statusTextView);
        TextView typeTextField = rowView.findViewById(R.id.deviceTypeTextView);
        TextView dateTextField = rowView.findViewById(R.id.dateTextView);

        Device device = dataSource.get(i);

        ImageView imageView = rowView.findViewById(R.id.imageView);
        switch (device.type) {
            case COFFEE_MACHINE:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_vendingmachine));
                break;
            case PRINTER:
            case UNKNOWN:
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_printer));
                break;
        }

        idTextField.setText(context.getResources().getString(R.string.device_id, device.deviceId));
        typeTextField.setText(DeviceType.toString(context, device.type));
        statusTextField.setText(DeviceStatus.toString(context, device.getStatus()));
        String formattedDate = dateFormat.format(device.getLastKnownUpdate());
        dateTextField.setText(context.getResources().getString(R.string.last_updated, formattedDate));

        if (DeviceStatus.DEFECT == dataSource.get(i).getStatus()) {
            rowView.setBackgroundColor(Color.LTGRAY);
        } else{
            rowView.setBackgroundColor(Color.WHITE);
        }

        return rowView;
    }
}
