package org.neo4j.collection;

import org.eclipse.collections.api.LazyLongIterable;
import org.eclipse.collections.api.LongIterable;
import org.eclipse.collections.api.bag.primitive.MutableLongBag;
import org.eclipse.collections.api.block.function.primitive.LongToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectLongToObjectFunction;
import org.eclipse.collections.api.block.predicate.primitive.LongPredicate;
import org.eclipse.collections.api.block.procedure.primitive.LongProcedure;
import org.eclipse.collections.api.iterator.MutableLongIterator;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.primitive.ImmutableLongSet;
import org.eclipse.collections.api.set.primitive.LongSet;
import org.eclipse.collections.api.set.primitive.MutableLongSet;
import org.eclipse.collections.impl.factory.primitive.LongSets;
import org.eclipse.collections.impl.set.mutable.primitive.SynchronizedLongSet;
import org.eclipse.collections.impl.set.mutable.primitive.UnmodifiableLongSet;

import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.neo4j.util.VisibleForTesting;

import static java.util.Objects.requireNonNull;

public class LazyInitMutableLongSet implements MutableLongSet
{
    private static final MutableLongSet EMPTY = LongSets.mutable.empty().asUnmodifiable();

    private final LazyInitWriteable<MutableLongSet> set;

    public LazyInitMutableLongSet( int initCapacity, IntFunction<MutableLongSet> ctor )
    {
        this( () -> requireNonNull( ctor ).apply( initCapacity ) );
    }

    public LazyInitMutableLongSet( Supplier<MutableLongSet> ctor )
    {
        this( ctor, EMPTY );
    }

    @VisibleForTesting
    LazyInitMutableLongSet( Supplier<MutableLongSet> ctor, MutableLongSet dummy )
    {
        this.set = new LazyInitWriteable<>( ctor, dummy );
    }

    @Override
    public MutableLongIterator longIterator()
    {
        return set.current().longIterator();
    }

    @Override
    public long[] toArray()
    {
        return set.current().toArray();
    }

    @Override
    public boolean contains( long value )
    {
        return set.current().contains( value );
    }

    @Override
    public boolean containsAll( long... elements )
    {
        return set.current().containsAll( elements );
    }

    @Override
    public boolean containsAll( LongIterable elements )
    {
        return set.current().containsAll( elements );
    }

    @Override
    public void forEach( LongProcedure procedure )
    {
        set.current().forEach( procedure );
    }

    @Override
    public void each( LongProcedure procedure )
    {
        set.current().each( procedure );
    }

    @Override
    public boolean add( long element )
    {
        return set.writeable().add( element );
    }

    @Override
    public boolean addAll( long... elements )
    {
        return set.writeable().addAll( elements );
    }

    @Override
    public boolean addAll( LongIterable elements )
    {
        return set.writeable().addAll( elements );
    }

    @Override
    public boolean remove( long value )
    {
        return set.current().remove( value );
    }

    @Override
    public boolean removeAll( LongIterable elements )
    {
        return set.current().removeAll( elements );
    }

    @Override
    public boolean removeAll( long... elements )
    {
        return set.current().removeAll( elements );
    }

    @Override
    public boolean retainAll( LongIterable elements )
    {
        return set.current().retainAll( elements );
    }

    @Override
    public boolean retainAll( long... elements )
    {
        return set.current().retainAll( elements );
    }

    @Override
    public void clear()
    {
        set.current().clear();
    }

    @Override
    public MutableLongSet select( LongPredicate predicate )
    {
        return set.current().select( predicate );
    }

    @Override
    public MutableLongSet reject( LongPredicate predicate )
    {
        return set.current().reject( predicate );
    }

    @Override
    public <V> MutableSet<V> collect( LongToObjectFunction<? extends V> function )
    {
        return set.current().collect( function );
    }

    @Override
    public long detectIfNone( LongPredicate predicate, long ifNone )
    {
        return set.current().detectIfNone( predicate, ifNone );
    }

    @Override
    public int count( LongPredicate predicate )
    {
        return set.current().count( predicate );
    }

    @Override
    public boolean anySatisfy( LongPredicate predicate )
    {
        return set.current().anySatisfy( predicate );
    }

    @Override
    public boolean allSatisfy( LongPredicate predicate )
    {
        return set.current().allSatisfy( predicate );
    }

    @Override
    public boolean noneSatisfy( LongPredicate predicate )
    {
        return set.current().noneSatisfy( predicate );
    }

    @Override
    public MutableLongList toList()
    {
        return set.current().toList();
    }

    @Override
    public MutableLongSet toSet()
    {
        return set.current().toSet();
    }

    @Override
    public MutableLongBag toBag()
    {
        return set.current().toBag();
    }

    @Override
    public LazyLongIterable asLazy()
    {
        return set.current().asLazy();
    }

    @Override
    public <T> T injectInto( T injectedValue, ObjectLongToObjectFunction<? super T, ? extends T> function )
    {
        return set.current().injectInto( injectedValue, function );
    }

    @Override
    public long sum()
    {
        return set.current().sum();
    }

    @Override
    public long max()
    {
        return set.current().max();
    }

    @Override
    public long maxIfEmpty( long defaultValue )
    {
        return set.current().maxIfEmpty( defaultValue );
    }

    @Override
    public long min()
    {
        return set.current().min();
    }

    @Override
    public long minIfEmpty( long defaultValue )
    {
        return set.current().minIfEmpty( defaultValue );
    }

    @Override
    public double average()
    {
        return set.current().average();
    }

    @Override
    public double median()
    {
        return set.current().median();
    }

    @Override
    public long[] toSortedArray()
    {
        return set.current().toSortedArray();
    }

    @Override
    public MutableLongList toSortedList()
    {
        return set.current().toSortedList();
    }

    @Override
    public MutableLongSet with( long element )
    {
        set.writeable().with( element );
        return this;
    }

    @Override
    public MutableLongSet without( long element )
    {
        set.current().without( element );
        return this;
    }

    @Override
    public MutableLongSet withAll( LongIterable elements )
    {
        set.writeable().withAll( elements );
        return this;
    }

    @Override
    public MutableLongSet withoutAll( LongIterable elements )
    {
        set.current().withoutAll( elements );
        return this;
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
    public LongSet freeze()
    {
        return set.current().freeze();
    }

    @Override
    public ImmutableLongSet toImmutable()
    {
        return set.current().toImmutable();
    }

    @Override
    public int size()
    {
        return set.current().size();
    }

    @Override
    public void appendString( Appendable appendable, String start, String separator, String end )
    {
        set.current().appendString( appendable, start, separator, end );
    }
}
