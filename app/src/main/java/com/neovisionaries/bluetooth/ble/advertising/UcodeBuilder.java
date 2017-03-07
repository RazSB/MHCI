package com.neovisionaries.bluetooth.ble.advertising;


/**
 * Builder for {@link Ucode}.
 *
 * @since 1.1
 */
class UcodeBuilder implements ADManufacturerSpecificBuilder
{
    @Override
    public ADManufacturerSpecific build(int length, int type, byte[] data, int companyId)
    {
        return Ucode.create(length, type, data, companyId);
    }
}
