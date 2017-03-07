package com.neovisionaries.bluetooth.ble.advertising;


/**
 * AD structure of type <i>Manufacturer Specific Data</i> (0xFF).
 */
public class ADManufacturerSpecific extends ADStructure
{
    private static final long serialVersionUID = 1L;
    private static final String STRING_FORMAT =
        "ADManufacturerSpecific(Length=%d,Type=0x%02X,CompanyID=0x%04X)";

    private int mCompanyId;


    /**
     * Constructor to create an instance with length=3, type=0xFF,
     * data={0xFF,0xFF}, and companyId=0xFFFF.
     */
    public ADManufacturerSpecific()
    {
        // Type 0xFF means "Manufacturer Specific Data".
        //
        // The meaning of Company ID 0xFFFF depends on the context,
        // the specification says. Note that 0 has been assigned to
        // "Ericsson Technology Licensing", so 0 should not be used
        // as the default value.
        this(3, 0xFF, new byte[]{ (byte)0xFF, (byte)0xFF }, 0xFFFF);
    }


    /**
     * Constructor.
     *
     * @param length
     *         The length of the AD structure.
     *
     * @param type
     *         The AD type. Should be 0xFF (Manufacturer Specific Data).
     *
     * @param companyId
     *         The company ID.
     *
     * @param data
     *         The AD data. Note that the value of the first two bytes represents
     *         the company ID.
     */
    public ADManufacturerSpecific(int length, int type, byte[] data, int companyId)
    {
        super(length, type, data);

        // Company ID.
        mCompanyId = companyId;
    }


    /**
     * Get the company ID.
     *
     * <p>
     * The list of official company IDs can be found at
     * <a href="https://www.bluetooth.org/en-us/specification/assigned-numbers/company-identifiers"
     * >Company Identifiers</a>.
     * </p>
     *
     * @return
     *         The company ID.
     */
    public int getCompanyId()
    {
        return mCompanyId;
    }


    /**
     * Set the company ID.
     *
     * @param companyId
     *         The company ID.
     */
    public void setCompanyId(int companyId)
    {
        mCompanyId = companyId;
    }


    @Override
    public String toString()
    {
        return String.format(STRING_FORMAT, getLength(), getType(), mCompanyId);
    }
}
