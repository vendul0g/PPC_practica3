package addressCalculator;

import java.net.*;

public class AddressCalculator {
	private static final String INTERFACE = "wlo1";
		
    public static InetAddress getBroadcastAddress() {
        NetworkInterface networkInterface;
		try {
			networkInterface = NetworkInterface.getByName(INTERFACE);
		} catch (SocketException e) {
			System.err.println("Interfaz "+INTERFACE+" no existe");
			return null;
		}

        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
            InetAddress broadcast = interfaceAddress.getBroadcast();
            if (broadcast != null) {
                return broadcast;
            }
        }

        System.err.println("Interface "+INTERFACE+" not found");
        return null;
    }
}
