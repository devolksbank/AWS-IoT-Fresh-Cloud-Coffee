package devolksbank.nl.statecloud.model;

import android.content.Context;

import devolksbank.nl.statecloud.R;

/**
 * Describes the status of a device.
 */
public enum DeviceStatus {

    DEFECT,
    WORKING,
    UNKNOWN;

    public static String toString(Context context, DeviceStatus status) {
        switch (status) {
            case DEFECT:
                return context.getString(R.string.device_status_defect);
            case WORKING:
                return context.getString(R.string.device_status_working);
            case UNKNOWN:
                return context.getString(R.string.device_status_unknown);
        }
        return null;
    }

    public static DeviceStatus fromString(Context context, String string) {
        if (null == string || string.isEmpty() || toString(context, UNKNOWN).equalsIgnoreCase(string)) {
            return UNKNOWN;
        } else if (toString(context, DEFECT).equalsIgnoreCase(string)) {
            return DEFECT;
        } else if (toString(context, WORKING).equalsIgnoreCase(string)) {
            return WORKING;
        }

        throw new IllegalArgumentException(context.getResources().getString(R.string.unknown_device_status_alert, string));
    }
}
