package com.neovisionaries.bluetooth.ble.advertising;


/**
 * Builder for {@link IBeacon}.
 */
class IBeaconBuilder implements ADManufacturerSpecificBuilder
{
    @Override
    public ADManufacturerSpecific build(int length, int type, byte[] data, int companyId)
    {
        return IBeacon.create(length, type, data, companyId);
    }
}
