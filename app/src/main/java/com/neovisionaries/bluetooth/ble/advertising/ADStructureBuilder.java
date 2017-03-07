package com.neovisionaries.bluetooth.ble.advertising;


/**
 * The interface that a builder of AD structure has to implement.
 */
public interface ADStructureBuilder
{
    /**
     * Build an AD structure.
     *
     * @param length
     *         The length of the AD structure. It is the value
     *         of the first octet of the byte sequence of the
     *         AD structure.
     *
     * @param type
     *         The AD type. It is the value of the second octet
     *         of the byte sequence of the AD structure.
     *
     * @param data
     *         The AD data. It is the third and following octets
     *         of the byte sequence of the AD structure.
     *
     * @return
     *         An instance of {@link ADStructure} or its subclass.
     *         If the builder fails to build an instance,
     *         {@code null} should be returned.
     */
    ADStructure build(int length, int type, byte[] data);
}
