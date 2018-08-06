package devolksbank.nl.statecloud.services.listeners;

import java.util.ArrayList;

import devolksbank.nl.statecloud.model.Device;

/**
 * Created by casper on 6/8/18.
 */

public interface GetDevicesCompleteListener {
    void getDevicesComplete(ArrayList<Device> devices);
}
