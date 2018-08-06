package devolksbank.nl.statecloud.model;

import java.util.Date;

/**
 * This class describes a device of which the status can be shown.
 */
public class Device {
    public final DeviceType type;
    public final int deviceId;
    private final Date lastKnownUpdate;
    private DeviceStatus status;

    /**
     * Constructor for creating a device.
     * @param type the {@link DeviceType} of this device
     * @param deviceID the id of this device, described by an {@link Integer}
     * @param status the {@link DeviceStatus} of this device
     */
    public Device(DeviceType type, int deviceID, DeviceStatus status, double timestamp) {
        this.type = type;
        this.deviceId = deviceID;
        this.status = status;
        this.lastKnownUpdate = new Date((long) timestamp);
    }

    /**
     * @return the {@link DeviceStatus} of this device
     */
    public DeviceStatus getStatus(){
        return status;
    }

    /**
     * Sets the status of this device.
     * @param status the {@link DeviceStatus} which should be assigned to this device
     */
    public void setStatus(DeviceStatus status){
        this.status = status;
    }


    /**
     * Returns the last known update time of this device
     *
     * @return a {@link Date} describing the last known update time.
     */
    public Date getLastKnownUpdate() {
        return this.lastKnownUpdate;
    }

    @Override
    public String toString() {
        return this.type + "," + this.deviceId + "," + this.status;
    }
}
