package com.neovisionaries.bluetooth.ble.util;


/**
 * Utility for byte arrays.
 *
 * @since 1.5
 */
public class Bytes
{
    private static final char[] UPPER_HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private static final char[] LOWER_HEX_CHARS = "0123456789abcdef".toCharArray();


    private Bytes()
    {
    }


    /**
     * Parse 2 bytes in big endian byte order as an {@code int}.
     */
    public static int parseBE2BytesAsInt(byte[] data, int offset)
    {
        int value = ((data[offset + 0] & 0xFF) << 8)
                  | ((data[offset + 1] & 0xFF) << 0);

        return value;
    }


    /**
     * Parse 4 bytes in big endian byte order as an {@code int}.
     */
    public static int parseBE4BytesAsInt(byte[] data, int offset)
    {
        int value = ((data[offset + 0] & 0xFF) << 24)
                  | ((data[offset + 1] & 0xFF) << 16)
                  | ((data[offset + 2] & 0xFF) <<  8)
                  | ((data[offset + 3] & 0xFF));

        return value;
    }


    /**
     * Parse 4 bytes in big endian byte order as an unsigned integer.
     */
    public static long parseBE4BytesAsUnsigned(byte[] data, int offset)
    {
        long value = ((long)(data[offset + 0] & 0xFF) << 24)
                   | ((long)(data[offset + 1] & 0xFF) << 16)
                   | ((long)(data[offset + 2] & 0xFF) <<  8)
                   | ((long)(data[offset + 3] & 0xFF) <<  0);

        return value;
    }


    /**
     * Convert 2 bytes in fixed point format to {@code float}.
     *
     * @see <a href="https://courses.cit.cornell.edu/ee476/Math/"
     *      >Cornell University, Electrical Engineering 476,
     *      Fixed Point mathematical functions in GCC and assembler</a>
     */
    public static float convertFixedPointToFloat(byte[] data, int offset)
    {
        return (data[offset] + ((data[offset + 1] & 0xFF) / 256.0F));
    }


    /**
     * Convert a byte array to a hex string.
     *
     * @param data
     *         An input byte array.
     *
     * @param upper
     *         {@code true} to generate a upper-case hex string.
     *         {@code false} to generate a lower-case hex string.
     *
     * @return
     *         A hex string. if {@code data} is {@code null},
     *         {@code null} is returned.
     */
    public static String toHexString(byte[] data, boolean upper)
    {
        if (data == null)
        {
            return null;
        }

        char[] table = (upper ? UPPER_HEX_CHARS : LOWER_HEX_CHARS);
        char[] chars = new char[data.length * 2];

        for (int i = 0; i < data.length; ++i)
        {
            chars[i * 2    ] = table[ (data[i] & 0xF0) >> 4 ];
            chars[i * 2 + 1] = table[ (data[i] & 0x0F)      ];
        }

        return new String(chars);
    }
}
