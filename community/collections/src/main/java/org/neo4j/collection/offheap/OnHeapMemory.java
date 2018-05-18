package org.neo4j.collection.offheap;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.neo4j.memory.MemoryAllocationTracker;

import static java.lang.Math.toIntExact;

class OnHeapMemory implements Memory
{
    private final ByteBuffer data;

    private OnHeapMemory( int size )
    {
        this.data = ByteBuffer.allocate( size );
    }

    public static OnHeapMemory allocate( long size, MemoryAllocationTracker unused )
    {
        return new OnHeapMemory( toIntExact( size ) );
    }

    @Override
    public long readLong( long offset )
    {
        return data.getLong( toIntExact( offset ) );
    }

    @Override
    public void writeLong( long offset, long value )
    {
        data.putLong( toIntExact( offset ), value );
    }

    @Override
    public void clear()
    {
        Arrays.fill( data.array(), (byte) 0 );
    }

    @Override
    public long size()
    {
        return 0;
    }

    @Override
    public void free()
    {
        // nop
    }
}
