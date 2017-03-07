package com.neovisionaries.bluetooth.ble.advertising;


/**
 * Builder for {@link ServiceData} instances.
 *
 * @since 1.5
 */
class ServiceDataBuilder implements ADStructureBuilder
{
    @Override
    public ADStructure build(int length, int type, byte[] data)
    {
        return new ServiceData(length, type, data);
    }
}
