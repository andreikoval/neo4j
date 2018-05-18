package org.neo4j.collection.offheap;

public interface Memory
{
    long readLong( long offset );

    void writeLong( long offset, long value );

    void clear();

    long size();

    void free();
}
