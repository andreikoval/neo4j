package org.neo4j.collection.offheap;

import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.factory.primitive.LongLists;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import static org.eclipse.collections.impl.list.mutable.primitive.LongArrayList.newListWith;
import static org.eclipse.collections.impl.set.mutable.primitive.LongHashSet.newSetWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.neo4j.collection.offheap.LinearProbeLongHashSet.DEFAULT_CAPACITY;
import static org.neo4j.collection.offheap.LinearProbeLongHashSet.REMOVALS_RATIO;

class LinearProbeLongHashSetTest
{
    @Test
    void addRemoveContainsInBitmap()
    {
        final LinearProbeLongHashSet set = spy( newSet() );

        assertFalse( set.contains( 1 ) );
        assertTrue( set.add( 1 ) );
        assertTrue( set.contains( 1 ) );
        assertFalse( set.add( 1 ) );
        assertEquals( 1, set.size() );

        assertFalse( set.contains( 2 ) );
        assertTrue( set.add( 2 ) );
        assertTrue( set.contains( 2 ) );
        assertFalse( set.add( 2 ) );
        assertEquals( 2, set.size() );

        assertFalse( set.contains( 3 ) );
        assertFalse( set.remove( 3 ) );
        assertEquals( 2, set.size() );

        assertEquals( newSetWith( 1, 2 ), set );

        assertTrue( set.remove( 1 ) );
        assertFalse( set.contains( 1 ) );
        assertEquals( 1, set.size() );

        assertTrue( set.remove( 2 ) );
        assertFalse( set.contains( 2 ) );
        assertEquals( 0, set.size() );

        verify( set, never() ).addToMemory( anyLong() );
        verify( set, never() ).removeFromMemory( anyLong() );
    }

    @Test
    void addRemoveContainsInMemory()
    {
        final LinearProbeLongHashSet set = spy( newSet() );

        assertFalse( set.contains( 100 ) );
        assertTrue( set.add( 100 ) );
        assertTrue( set.contains( 100 ) );
        assertFalse( set.add( 100 ) );
        assertEquals( 1, set.size() );

        assertFalse( set.contains( 200 ) );
        assertTrue( set.add( 200 ) );
        assertTrue( set.contains( 200 ) );
        assertFalse( set.add( 200 ) );
        assertEquals( 2, set.size() );

        assertFalse( set.contains( 300 ) );
        assertFalse( set.remove( 300 ) );
        assertEquals( 2, set.size() );

        assertEquals( newSetWith( 100, 200 ), set );

        assertTrue( set.remove( 100 ) );
        assertFalse( set.contains( 100 ) );
        assertEquals( 1, set.size() );

        assertTrue( set.remove( 200 ) );
        assertFalse( set.contains( 200 ) );
        assertEquals( 0, set.size() );

        verify( set, never() ).addToBitmap( anyLong() );
        verify( set, never() ).removeFromBitmap( anyLong() );
    }

    @Test
    void clear()
    {
        final LinearProbeLongHashSet set = newSet();

        set.addAll( 1, 2, 3 );
        assertEquals( 3, set.size() );

        set.clear();
        assertEquals( 0, set.size() );

        set.clear();
        assertEquals( 0, set.size() );

        set.addAll( 4, 5, 6 );
        assertEquals( 3, set.size() );

        set.clear();
        assertEquals( 0, set.size() );
    }

    @Test
    void grow()
    {
        final LinearProbeLongHashSet set = spy( newSet() );
        for ( int i = 0; i < DEFAULT_CAPACITY; i++ )
        {
            assertTrue( set.add( 100 + i ) );
        }
        verify( set ).growAndRehash();
    }

    @Test
    void rehashWhenTooManyRemovals()
    {
        final LinearProbeLongHashSet set = spy( newSet() );
        final int numOfElements = DEFAULT_CAPACITY / 2;
        final int removalsToTriggerRehashing = DEFAULT_CAPACITY / REMOVALS_RATIO;

        for ( int i = 0; i < numOfElements; i++ )
        {
            assertTrue( set.add( 100 + i ) );
        }

        assertEquals( numOfElements, set.size() );
        verify( set, never() ).rehashWithouGrow();
        verify( set, never() ).growAndRehash();

        for ( int i = 0; i < removalsToTriggerRehashing; i++ )
        {
            assertTrue( set.remove( 100 + i ) );
        }

        assertEquals( numOfElements - removalsToTriggerRehashing, set.size() );
        verify( set ).rehashWithouGrow();
        verify( set, never() ).growAndRehash();
    }

    @TestFactory
    Collection<DynamicTest> collisions()
    {
        final ImmutableLongList collisions = generateCollisions( 5 );
        final long a = collisions.get( 0 );
        final long b = collisions.get( 1 );
        final long c = collisions.get( 2 );
        final long d = collisions.get( 3 );
        final long e = collisions.get( 4 );

        return Arrays.asList(
                dynamicTest( "add all", testSet( set ->
                {
                    set.addAll( collisions );
                    assertEquals( collisions, set.toList() );
                } ) ),
                dynamicTest( "add all reversed", testSet( set ->
                {
                    set.addAll( collisions.toReversed() );
                    assertEquals( collisions.toReversed(), set.toList() );
                } ) ),
                dynamicTest( "add all, remove last", testSet( set ->
                {
                    set.addAll( collisions );
                    set.remove( e );
                    assertEquals( newListWith( a, b, c, d ), set.toList() );
                } ) ),
                dynamicTest( "add all, remove first", testSet( set ->
                {
                    set.addAll( collisions );
                    set.remove( a );
                    assertEquals( newListWith( b, c, d, e ), set.toList() );
                } ) ),
                dynamicTest( "add all, remove middle", testSet( set ->
                {
                    set.addAll( collisions );
                    set.removeAll( b, d );
                    assertEquals( newListWith( a, c, e ), set.toList() );
                } ) ),
                dynamicTest( "add all, remove middle 2", testSet( set ->
                {
                    set.addAll( collisions );
                    set.removeAll( a, c, e );
                    assertEquals( newListWith( b, d ), set.toList() );
                } ) ),
                dynamicTest( "add reuses removed head", testSet( set ->
                {
                    set.addAll( a, b, c );

                    set.remove( a );
                    assertEquals( newListWith( b, c ), set.toList() );

                    set.add( d );
                    assertEquals( newListWith( d, b, c ), set.toList() );
                } ) ),
                dynamicTest( "add reuses removed tail", testSet( set ->
                {
                    set.addAll( a, b, c );

                    set.remove( c );
                    assertEquals( newListWith( a, b ), set.toList() );

                    set.add( d );
                    assertEquals( newListWith( a, b, d ), set.toList() );
                } ) ),
                dynamicTest( "add reuses removed middle", testSet( set ->
                {
                    set.addAll( a, b, c );

                    set.remove( b );
                    assertEquals( newListWith( a, c ), set.toList() );

                    set.add( d );
                    assertEquals( newListWith( a, d, c ), set.toList() );
                } ) ),
                dynamicTest( "add reuses removed middle 2", testSet( set ->
                {
                    set.addAll( a, b, c, d, e );

                    set.removeAll( b, c );
                    assertEquals( newListWith( a, d, e ), set.toList() );

                    set.addAll( c, b );
                    assertEquals( newListWith( a, c, b, d, e ), set.toList() );
                } ) ),
                dynamicTest( "rehashing compacts sparse sentinels", testSet( set ->
                {
                    set.addAll( a, b, c, d, e );

                    set.removeAll( b, d, e );
                    assertEquals( newListWith( a, c ), set.toList() );

                    set.addAll( b, d, e );
                    assertEquals( newListWith( a, b, c, d, e ), set.toList() );

                    set.removeAll( b, d, e );
                    assertEquals( newListWith( a, c ), set.toList() );

                    set.rehashWithouGrow();
                    set.addAll( e, d, b );
                    assertEquals( newListWith( a, c, e, d, b ), set.toList() );
                } ) )
        );
    }

    private static Executable testSet( Consumer<LinearProbeLongHashSet> test )
    {
        return () -> test.accept( newSet() );
    }

    private static ImmutableLongList generateCollisions( int n )
    {
        long v = 1984;
        final LinearProbeLongHashSet set = newSet();
        final int h = set.hashAndMask( v );
        final MutableLongList elements = LongLists.mutable.with( v );

        while ( elements.size() < n )
        {
            ++v;
            if ( set.hashAndMask( v ) == h )
            {
                elements.add( v );
            }
        }

        return elements.toImmutable();
    }

    private static LinearProbeLongHashSet newSet()
    {
        return new LinearProbeLongHashSet( OnHeapMemory::allocate );
    }
}
