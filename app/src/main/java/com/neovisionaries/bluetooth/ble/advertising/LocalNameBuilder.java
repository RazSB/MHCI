package com.neovisionaries.bluetooth.ble.advertising;


/**
 * Builder for {@link LocalName} instances.
 *
 * @since 1.3
 */
class LocalNameBuilder implements ADStructureBuilder
{
    @Override
    public ADStructure build(int length, int type, byte[] data)
    {
        return new LocalName(length, type, data);
    }
}
