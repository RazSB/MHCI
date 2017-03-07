package com.neovisionaries.bluetooth.ble.advertising;


class FlagsBuilder implements ADStructureBuilder
{
    @Override
    public ADStructure build(int length, int type, byte[] data)
    {
        return new Flags(length, type, data);
    }
}
