package css.out.device;

import java.util.HashMap;

public class DeviceManagement {

    public HashMap<String, Device> devices = new HashMap<String, Device>();

    public void addDevice(String name) {
        devices.put(name, new Device(name));
    }

}
