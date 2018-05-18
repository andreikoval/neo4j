package org.neo4j.collection.offheap;

import org.neo4j.memory.GlobalMemoryTracker;
import org.neo4j.memory.MemoryAllocationTracker;

@FunctionalInterface
public interface MemoryAllocator
{
    Memory allocate( long size, MemoryAllocationTracker tracker );

    default Memory allocate( long size ) {
        return allocate( size, GlobalMemoryTracker.INSTANCE );
    }
}
