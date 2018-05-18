package org.neo4j.collection.offheap;

import org.eclipse.collections.api.LongIterable;
import org.eclipse.collections.api.block.function.primitive.LongToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectLongToObjectFunction;
import org.eclipse.collections.api.block.predicate.primitive.LongPredicate;
import org.eclipse.collections.api.block.procedure.primitive.LongProcedure;
import org.eclipse.collections.api.iterator.MutableLongIterator;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.ImmutableLongSet;
import org.eclipse.collections.api.set.primitive.LongSet;
import org.eclipse.collections.api.set.primitive.MutableLongSet;
import org.eclipse.collections.impl.set.mutable.primitive.SynchronizedLongSet;
import org.eclipse.collections.impl.set.mutable.primitive.UnmodifiableLongSet;
import org.eclipse.collections.impl.set.primitive.AbstractLongSet;

import java.io.IOException;

import org.neo4j.graphdb.Resource;
import org.neo4j.util.VisibleForTesting;

import static java.util.Objects.requireNonNull;
import static org.neo4j.collection.offheap.Utils.ceilToPowerOf2;
import static org.neo4j.util.Preconditions.requirePositive;

public class LinearProbeLongHashSet extends AbstractLongSet implements MutableLongSet, Resource
{
    static final int DEFAULT_CAPACITY = 16;
    static final int REMOVALS_RATIO = 4;
    private static final double LOAD_FACTOR = 0.75;

    private static final long EMPTY = 0;
    private static final long REMOVED = 1;

    private final MemoryAllocator allocator;

    private Memory memory;
    private int capacity;
    private int resizeOccupancyThreshold;
    private int resizeRemovalsThreshold;

    private long bitmap;
    private int elementsInMemory;
    private int elementsInBitmap;
    private int removals;


//    private boolean frozen;

    public LinearProbeLongHashSet( MemoryAllocator allocator )
    {
        this( DEFAULT_CAPACITY, allocator );
    }

    LinearProbeLongHashSet( int minInitialCapacity, MemoryAllocator allocator )
    {
        this.allocator = requireNonNull( allocator );
        final int initialCapacity = ceilToPowerOf2( requirePositive( minInitialCapacity ) );
        allocateMemory( initialCapacity );
    }

    private static int hash( long value )
    {
        long h = value;
        h = ~h + (h << 18);
        h ^= h >>> 31;
        h *= 21;
        h ^= h >>> 11;
        h += h << 6;
        h ^= h >>> 22;
        return (int) h;
    }

    private static boolean fitsInBitmap( long element )
    {
        return element >= 0 && element < 64;
    }

    private static boolean isRealValue( long value )
    {
        return value != REMOVED && value != EMPTY;
    }

    @Override
    public boolean add( long element )
    {
        return fitsInBitmap( element ) ? addToBitmap( element ) : addToMemory( element );
    }

    @Override
    public boolean addAll( long... elements )
    {
        final int prevSize = size();
        for ( final long element : elements )
        {
            add( element );
        }
        return prevSize != size();
    }

    @Override
    public boolean addAll( LongIterable elements )
    {
        final int prevSize = size();
        elements.forEach( this::add );
        return prevSize != size();
    }

    @Override
    public boolean remove( long element )
    {
        return fitsInBitmap( element ) ? removeFromBitmap( element ) : removeFromMemory( element );
    }

    @Override
    public boolean removeAll( LongIterable elements )
    {
        final int prevSize = size();
        elements.forEach( this::remove );
        return prevSize != size();
    }

    @Override
    public boolean removeAll( long... elements )
    {
        final int prevSize = size();
        for ( final long element : elements )
        {
            remove( element );
        }
        return prevSize != size();
    }

    @Override
    public boolean retainAll( LongIterable elements )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll( long... source )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        // todo check frozen

        memory.clear();
        bitmap = 0;
        elementsInMemory = 0;
        elementsInBitmap = 0;
        removals = 0;
    }

    @Override
    public MutableLongIterator longIterator()
    {
        return new IteratorImpl( this );
    }

    @Override
    public long[] toArray()
    {
        final int[] i = {0};
        final long[] array = new long[size()];
        each( element -> array[i[0]++] = element );
        return array;
    }

    @Override
    public boolean contains( long element )
    {
        if ( fitsInBitmap( element ) )
        {
            return (bitmap & (1L << element)) != 0;
        }

        int idx = indexOf( element );
        for ( int i = 0; i < capacity; i++ )
        {
            final long valueAtIdx = getValue( idx );
            if ( valueAtIdx == element )
            {
                return true;
            }
            if ( valueAtIdx == EMPTY )
            {
                return false;
            }
            idx = mask( idx + 1 );
        }

        return false;
    }

    @Override
    public void forEach( LongProcedure procedure )
    {
        each( procedure );
    }

    @Override
    public void each( LongProcedure procedure )
    {
        final int size = size();
        int n = 0;
        if ( elementsInBitmap > 0 )
        {
            long bit = 1;
            for ( int i = 0; i < 64; i++ )
            {
                if ( (bitmap & bit) != 0 )
                {
                    procedure.accept( i );
                    ++n;
                }
                bit <<= 1;
            }
        }

        for ( int i = 0; i < capacity && n < size; i++ )
        {
            final long value = getValue( i );
            if ( isRealValue( value ) )
            {
                procedure.accept( value );
                ++n;
            }
        }
    }

    @Override
    public long detectIfNone( LongPredicate predicate, long ifNone )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean anySatisfy( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean allSatisfy( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean noneSatisfy( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T injectInto( T injectedValue, ObjectLongToObjectFunction<? super T, ? extends T> function )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sum()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long max()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long min()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet select( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet reject( LongPredicate predicate )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> MutableSet<V> collect( LongToObjectFunction<? extends V> function )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public LongSet freeze()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableLongSet toImmutable()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet with( long element )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet without( long element )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet withAll( LongIterable elements )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet withoutAll( LongIterable elements )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableLongSet asUnmodifiable()
    {
        return new UnmodifiableLongSet( this );
    }

    @Override
    public MutableLongSet asSynchronized()
    {
        return new SynchronizedLongSet( this );
    }

    @Override
    public int hashCode()
    {
        final int[] h = {0};
        each( element -> h[0] += element ^ element >>> 32 );
        return h[0];
    }

    @Override
    public int size()
    {
        return elementsInBitmap + elementsInMemory;
    }

    @Override
    public void appendString( Appendable appendable, String start, String separator, String end )
    {
        try
        {
            appendable.append( start );
            appendable.append( "offheap,size=" ).append( String.valueOf( size() ) );
            appendable.append( end );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public void close()
    {
        memory.free();
        memory = null;
    }

    @VisibleForTesting
    boolean removeFromMemory( long element )
    {
        final int idx = indexOf( element );
        final long valueAtIdx = memory.readLong( (long) idx << 3 );

        if ( valueAtIdx != element )
        {
            return false;
        }

        // todo check if frozen

        memory.writeLong( (long) idx << 3, REMOVED );
        --elementsInMemory;
        ++removals;

        if ( removals >= resizeRemovalsThreshold )
        {
            rehashWithouGrow();
        }

        return true;
    }

    @VisibleForTesting
    boolean removeFromBitmap( long element )
    {
        final long prevBitmap = bitmap;
        bitmap &= ~(1L << element);
        if ( bitmap != prevBitmap )
        {
            --elementsInBitmap;
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean addToBitmap( long element )
    {
        final long prevBitmap = bitmap;
        bitmap |= 1L << element;
        if ( bitmap != prevBitmap )
        {
            ++elementsInBitmap;
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean addToMemory( long element )
    {
        final int idx = indexOf( element );
        final long valueAtIdx = memory.readLong( (long) idx << 3 );

        if ( valueAtIdx == element )
        {
            return false;
        }

        if ( valueAtIdx == REMOVED )
        {
            --removals;
        }

        memory.writeLong( (long) idx << 3, element );
        ++elementsInMemory;

        // todo check if frozen

        if ( elementsInMemory >= resizeOccupancyThreshold )
        {
            growAndRehash();
        }

        return true;
    }

    @VisibleForTesting
    void growAndRehash()
    {
        final int newCapacity = capacity << 1;
        if ( newCapacity < capacity )
        {
            throw new RuntimeException( "Hash table reached capacity limit" );
        }
        rehash( newCapacity );
    }

    @VisibleForTesting
    void rehashWithouGrow()
    {
        rehash( capacity );
    }

    @VisibleForTesting
    int hashAndMask( long element )
    {
        return mask( hash( element ) );
    }

    private void rehash( int newCapacity )
    {
        final int prevCapacity = capacity;
        final Memory prevMemory = memory;
        elementsInMemory = 0;
        removals = 0;
        allocateMemory( newCapacity );

        for ( int i = 0; i < prevCapacity; i++ )
        {
            final long value = prevMemory.readLong( (long) i << 3 );
            if ( isRealValue( value ) )
            {
                add( value );
            }
        }

        prevMemory.free();
    }

    private long getValue( int idx )
    {
        return memory.readLong( (long) idx << 3 );
    }

    private void allocateMemory( int newCapacity )
    {
        capacity = newCapacity;
        resizeOccupancyThreshold = (int) (newCapacity * LOAD_FACTOR);
        resizeRemovalsThreshold = newCapacity / REMOVALS_RATIO;
        memory = allocator.allocate( (long) newCapacity << 3 );
    }

    private int indexOf( long element )
    {
        int idx = hashAndMask( element );
        int firstRemovedIdx = -1;
        long valueAtIdx;

        for ( int i = 0; i < capacity; i++ )
        {
            valueAtIdx = memory.readLong( (long) idx << 3 );

            if ( valueAtIdx == element )
            {
                return idx;
            }

            if ( valueAtIdx == EMPTY )
            {
                return firstRemovedIdx == -1 ? idx : firstRemovedIdx;
            }

            if ( valueAtIdx == REMOVED && firstRemovedIdx == -1 )
            {
                firstRemovedIdx = idx;
            }

            idx = mask( idx + 1 );
        }

        throw new AssertionError( "Failed to determine index for " + element );
    }

    private int mask( int key )
    {
        return key & (capacity - 1);
    }

    private static class IteratorImpl implements MutableLongIterator
    {
        private final Memory memory;

        private IteratorImpl( LinearProbeLongHashSet set )
        {
            this.memory = set.memory;
        }

        @Override
        public void remove()
        {

        }

        @Override
        public long next()
        {
            return 0;
        }

        @Override
        public boolean hasNext()
        {
            return false;
        }
    }
}
