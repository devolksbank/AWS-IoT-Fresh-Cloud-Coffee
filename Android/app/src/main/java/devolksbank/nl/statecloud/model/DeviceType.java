package devolksbank.nl.statecloud.model;

import android.content.Context;

import devolksbank.nl.statecloud.R;

/**
 * The type of a device.
 */
public enum DeviceType {

    COFFEE_MACHINE,
    PRINTER,
    UNKNOWN;

    public static String toString(Context context, DeviceType status) {
        switch (status) {
            case COFFEE_MACHINE:
                return context.getString(R.string.device_type_coffee_machine);
            case PRINTER:
                return context.getString(R.string.device_type_printer);
            case UNKNOWN:
                return context.getString(R.string.device_type_unknown);
        }
        return null;
    }

    public static DeviceType fromString(Context context, String string) {
        if (null == string || string.isEmpty() || toString(context, UNKNOWN).equalsIgnoreCase(string)) {
            return UNKNOWN;
        } else if (toString(context, COFFEE_MACHINE).equalsIgnoreCase(string)) {
            return COFFEE_MACHINE;
        } else if (toString(context, PRINTER).equalsIgnoreCase(string)) {
            return PRINTER;
        }
        throw new IllegalArgumentException(context.getResources().getString(R.string.unknown_device_type_alert, string));
    }
}
