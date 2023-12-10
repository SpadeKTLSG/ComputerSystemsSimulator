package css.out.device;

import core.process.Pcb;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class DeviceManagement {

    public HashMap<String ,Device> devices = new HashMap<String ,Device>();

    public void addDevice(String name){
        devices.put(name,new Device(name));
    }

}
