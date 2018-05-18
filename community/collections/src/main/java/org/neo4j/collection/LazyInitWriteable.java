package org.neo4j.collection;

import java.util.function.Supplier;

class LazyInitWriteable<W>
{
    private final Supplier<W> ctor;
    private final W dummy;
    private W current;

    LazyInitWriteable( Supplier<W> ctor, W dummy )
    {
        this.dummy = dummy;
        this.current = dummy;
        this.ctor = ctor;
    }

    W writeable()
    {
        if ( current == dummy )
        {
            current = ctor.get();
        }
        return current;
    }

    W current()
    {
        return current;
    }
}
