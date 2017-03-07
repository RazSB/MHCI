package com.neovisionaries.bluetooth.ble.advertising;


class TxPowerLevelBuilder implements ADStructureBuilder
{
    @Override
    public ADStructure build(int length, int type, byte[] data)
    {
        return new TxPowerLevel(length, type, data);
    }
}
