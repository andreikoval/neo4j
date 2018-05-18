package org.neo4j.collection.offheap;

import static java.lang.Integer.numberOfLeadingZeros;

class Utils
{

    private Utils()
    {
        // nop
    }

    static int ceilToPowerOf2( int i )
    {
        return 1 << (32 - numberOfLeadingZeros( i - 1 ));
    }
}
