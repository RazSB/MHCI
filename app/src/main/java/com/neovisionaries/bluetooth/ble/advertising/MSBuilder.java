package com.neovisionaries.bluetooth.ble.advertising;


import java.util.ArrayList;
import java.util.List;


/**
 * The base class for {@link ADManufacturerSpecificBuilder} implementations.
 */
public class MSBuilder implements ADManufacturerSpecificBuilder
{
    private final List<ADManufacturerSpecificBuilder> mBuilders =
        new ArrayList<ADManufacturerSpecificBuilder>();


    public MSBuilder()
    {
    }


    public MSBuilder(ADManufacturerSpecificBuilder... builders)
    {
        for (ADManufacturerSpecificBuilder builder : builders)
        {
            mBuilders.add(builder);
        }
    }


    @Override
    public ADManufacturerSpecific build(int length, int type, byte[] data, int companyId)
    {
        // For each builder.
        for (ADManufacturerSpecificBuilder builder : mBuilders)
        {
            // Let the builder build an AD structure.
            ADManufacturerSpecific structure = builder.build(length, type, data, companyId);

            // If the builder succeeded in building an AD structure.
            if (structure != null)
            {
                return structure;
            }
        }

        // None of the builders succeeded in building an AD structure.
        return null;
    }


    public void addBuilder(ADManufacturerSpecificBuilder builder)
    {
        if (builder == null)
        {
            return;
        }

        mBuilders.add(builder);
    }


    public void removeBuilder(ADManufacturerSpecificBuilder builder)
    {
        if (builder == null)
        {
            return;
        }

        mBuilders.remove(builder);
    }
}
