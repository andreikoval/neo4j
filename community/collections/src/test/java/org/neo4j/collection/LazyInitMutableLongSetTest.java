package org.neo4j.collection;

import org.eclipse.collections.api.LongIterable;
import org.eclipse.collections.api.PrimitiveIterable;
import org.eclipse.collections.api.collection.primitive.MutableLongCollection;
import org.eclipse.collections.api.set.primitive.MutableLongSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.primitive.LongSets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.eclipse.collections.impl.tuple.Tuples.pair;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

class LazyInitMutableLongSetTest
{
    @TestFactory
    @DisplayName( "Switch delegation from dummy to real impl after write operation" )
    Stream<DynamicTest> switchFromDummyToRealImplementationAfterWriting()
    {
        List<Pair<String, Consumer<MutableLongSet>>> methods = asList(
                pair( "longIterator", MutableLongCollection::longIterator ),
                pair( "toArray", LongIterable::toArray ),
                pair( "contains", set -> set.contains( 1 ) ),
                pair( "containsAll", set -> set.containsAll( 1 ) ),
                pair( "containsAll2", set -> set.containsAll( LongSets.immutable.empty() ) ),
                pair( "forEach", set -> set.forEach( String::valueOf ) ),
                pair( "each", set -> set.each( String::valueOf ) ),
                pair( "remove", set -> set.remove( 1 ) ),
                pair( "removeAll", set -> set.removeAll( 1 ) ),
                pair( "removeAll2", set -> set.removeAll( LongSets.immutable.empty() ) ),
                pair( "retainAll", set -> set.retainAll( 1 ) ),
                pair( "retainAll2", set -> set.retainAll( LongSets.immutable.empty() ) ),
                pair( "clear", MutableLongCollection::clear ),
                pair( "select", set -> set.select( x -> true ) ),
                pair( "reject", set -> set.reject( x -> true ) ),
                pair( "collect", set -> set.collect( String::valueOf ) ),
                pair( "detectIfNone", set -> set.detectIfNone( x -> true, 1 ) ),
                pair( "count", set -> set.count( x -> true ) ),
                pair( "anySatisfy", set -> set.anySatisfy( x -> true ) ),
                pair( "allSatisfy", set -> set.allSatisfy( x -> true ) ),
                pair( "noneSatisfy", set -> set.noneSatisfy( x -> true ) ),
                pair( "toList", LongIterable::toList ),
                pair( "toSet", LongIterable::toSet ),
                pair( "toBag", LongIterable::toBag ),
                pair( "asLazy", LongIterable::asLazy ),
                pair( "injectInto", set -> set.injectInto( 1, ( a, b ) -> 1 ) ),
                pair( "sum", LongIterable::sum ),
                pair( "max", LongIterable::max ),
                pair( "maxIfEmpty", set -> set.maxIfEmpty( 1 ) ),
                pair( "min", LongIterable::min ),
                pair( "minIfEmpty", set -> set.minIfEmpty( 1 ) ),
                pair( "average", LongIterable::average ),
                pair( "median", LongIterable::median ),
                pair( "toSortedArray", LongIterable::toSortedArray ),
                pair( "toSortedList", LongIterable::toSortedList ),
                pair( "without", set -> set.without( 1 ) ),
                pair( "withoutAll", set -> set.withoutAll( LongSets.immutable.empty() ) ),
                pair( "freeze", MutableLongSet::freeze ),
                pair( "toImmutable", MutableLongSet::toImmutable ),
                pair( "size", PrimitiveIterable::size )
        );

        return methods.stream().map( pair ->
        {
            final String methodName = pair.getOne();
            final Consumer<MutableLongSet> methodRef = pair.getTwo();

            return dynamicTest( methodName, () ->
            {
                final MutableLongSet dummy = mock( MutableLongSet.class );
                final MutableLongSet real = mock( MutableLongSet.class );
                final LazyInitMutableLongSet lazy = new LazyInitMutableLongSet( () -> real, dummy );

                methodRef.accept( lazy );
                methodRef.accept( verify( dummy ) );
                methodRef.accept( verify( real, never() ) );

                lazy.add( 1 );

                methodRef.accept( lazy );
                verifyNoMoreInteractions( dummy );
                methodRef.accept( verify( real ) );
            } );
        } );
    }

    @TestFactory
    @DisplayName( "Write operations force creating and using real impl" )
    Stream<DynamicTest> createAndUseRealImplementationAfterWriting()
    {
        List<Pair<String, Consumer<MutableLongSet>>> methods = asList(
                pair( "add", set -> set.add( 1 ) ),
                pair( "addAll", set -> set.addAll( 1 ) ),
                pair( "addAll", set -> set.addAll( LongSets.immutable.empty() ) ),
                pair( "with", set -> set.with( 1 ) ),
                pair( "withAll", set -> set.withAll( LongSets.immutable.empty() ) )
        );

        return methods.stream().map( pair ->
        {
            final String methodName = pair.getOne();
            final Consumer<MutableLongSet> methodRef = pair.getTwo();

            return dynamicTest( methodName, () ->
            {
                final MutableLongSet dummy = mock( MutableLongSet.class );
                final MutableLongSet real = mock( MutableLongSet.class );
                @SuppressWarnings( "Convert2Lambda" ) final Supplier<MutableLongSet> ctor = spy( new Supplier<MutableLongSet>()
                {
                    @Override
                    public MutableLongSet get()
                    {
                        return real;
                    }
                } );

                final LazyInitMutableLongSet lazy = new LazyInitMutableLongSet( ctor, dummy );

                methodRef.accept( lazy );
                verify( ctor ).get();
                methodRef.accept( verify( real ) );
                verifyZeroInteractions( dummy );
            } );
        } );
    }
}
