package org.neo4j.collection.offheap;

import org.neo4j.memory.MemoryAllocationTracker;
import org.neo4j.unsafe.impl.internal.dragons.UnsafeUtil;

import static org.neo4j.unsafe.impl.internal.dragons.UnsafeUtil.getLong;
import static org.neo4j.unsafe.impl.internal.dragons.UnsafeUtil.putLong;
import static org.neo4j.unsafe.impl.internal.dragons.UnsafeUtil.setMemory;
import static org.neo4j.util.Preconditions.requirePositive;

public class OffHeapMemory implements Memory
{
    private final long unalignedAddr;
    private final long unalignedSize;
    private final long addr;
    private final long size;
    private final MemoryAllocationTracker tracker;

    OffHeapMemory( long unalignedAddr, long unalignedSize, long addr, long size, MemoryAllocationTracker tracker )
    {
        this.unalignedAddr = unalignedAddr;
        this.unalignedSize = unalignedSize;
        this.addr = addr;
        this.size = size;
        this.tracker = tracker;
    }

    public static OffHeapMemory allocate( long size, MemoryAllocationTracker tracker )
    {
        final long unalignedSize = requirePositive( size ) + Long.BYTES - 1;
        final long unalignedAddr = UnsafeUtil.allocateMemory( unalignedSize, tracker );
        setMemory( unalignedAddr, unalignedSize, (byte) 0 );
        final long addr = UnsafeUtil.alignedMemory( unalignedAddr, Long.BYTES );
        return new OffHeapMemory( unalignedAddr, unalignedSize, addr, size, tracker );
    }


    @Override
    public long readLong( long offset )
    {
        return getLong( addr + offset );
    }

    @Override
    public void writeLong( long offset, long value )
    {
        putLong( addr + offset, value );
    }

    @Override
    public void clear()
    {
        setMemory( addr, size, (byte) 0 );
    }

    @Override
    public long size()
    {
        return size;
    }

    @Override
    public void free()
    {
        UnsafeUtil.free( unalignedAddr, unalignedSize, tracker );
    }
}
