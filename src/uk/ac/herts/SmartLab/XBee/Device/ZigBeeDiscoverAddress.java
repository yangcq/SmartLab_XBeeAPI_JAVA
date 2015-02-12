package uk.ac.herts.SmartLab.XBee.Device;

import uk.ac.herts.SmartLab.XBee.Response.ICommandResponse;
import uk.ac.herts.SmartLab.XBee.Type.DeviceType;

    public class ZigBeeDiscoverAddress extends XBeeDiscoverAddress
    {
        // total 8 bytes
        // 2 bytes of ParentAddress16 + 1 byte of Type + 1 byte of Status (Reserved) + 2 bytes of ProfileID + 2 bytes of ManufacturerID
        private byte[] zigbeeAdditional;

        public int ManufacturerID()
        {
            return (zigbeeAdditional[6] << 8) | zigbeeAdditional[7];
        }

        public int GetProfileID()
        {
            return (zigbeeAdditional[4] << 8) | zigbeeAdditional[5];
        }

        public int GetParentNetworkAddress16()
        {
            return (zigbeeAdditional[0] << 8) | zigbeeAdditional[1];
        }

        public DeviceType GetDeviceType()
        {
            return DeviceType.parse(zigbeeAdditional[2]);
        }

        /// <summary>
        /// extension method for convert DN / ND (with or without NI String) response to address 
        /// </summary>
        /// <param name="response">muset be non null parameter</param>
        /// <returns></returns>
        public static ZigBeeDiscoverAddress Parse(ICommandResponse response)
        {
            byte[] message = response.GetParameter();
            if (message != null)
                if (response.GetRequestCommand().toString().equalsIgnoreCase("ND"))
                {
                    int offset = message.length - 8;
                    ZigBeeDiscoverAddress device = new ZigBeeDiscoverAddress();

                    device.value[0] = message[2];
                    device.value[1] = message[3];
                    device.value[2] = message[4];
                    device.value[3] = message[5];
                    device.value[4] = message[6];
                    device.value[5] = message[7];
                    device.value[6] = message[8];
                    device.value[7] = message[9];

                    device.value[8] = message[0];
                    device.value[9] = message[1];

                    device.NIString = new String(message, 10, message.length - 18);
                    System.arraycopy(message, offset, device.zigbeeAdditional, 0, 8);

                    return device;
                }
            return null;
        }
    }