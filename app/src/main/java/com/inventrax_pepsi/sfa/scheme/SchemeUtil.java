package com.inventrax_pepsi.sfa.scheme;

import com.inventrax_pepsi.sfa.pojos.Scheme;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by android on 6/13/2016.
 */
public class SchemeUtil {


    public static List<Scheme> getSortedSchemes(List<Scheme> schemes)
    {
       Collections.sort(schemes, new Comparator<Scheme>() {
            @Override
            public int compare(Scheme lhs, Scheme rhs) {
                return (lhs.getSchemeId() >= rhs.getSchemeId()) ? -1 : 1;
            }
        });

        return schemes;
    }

}
