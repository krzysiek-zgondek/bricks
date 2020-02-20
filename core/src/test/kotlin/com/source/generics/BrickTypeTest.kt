package com.source.generics

import com.source.bricks.generics.BrickType
import com.source.bricks.generics.toType
import com.source.bricks.generics.type
import org.junit.Test

/**
 * @author SourceOne on 20.12.2019
 * @project Bricks
 */
class BrickTypeTest {
    @Test
    fun `1 layer plain objects generics`() {
        val base: BrickType<String> = object : BrickType<String>() {}
        val same: BrickType<String> = object : BrickType<String>() {}
        val different: BrickType<Int> = object : BrickType<Int>() {}

        assert(base == base) { "same instance should be equal" }
        assert(base == same) { "same underlying type should be equal" }
        assert(base != different) { "different underlying types should be different" }
    }

    @Test
    fun `2 layer plain objects generics`() {
        val base: BrickType<HashSet<String>> = object : BrickType<HashSet<String>>() {}
        val same: BrickType<HashSet<String>> = object : BrickType<HashSet<String>>() {}
        val different: BrickType<HashSet<Int>> = object : BrickType<HashSet<Int>>() {}

        assert(base == base) { "same instance should be equal" }
        assert(base == same) { "same underlying type should be equal" }
        assert(base != different) { "different underlying types should be different" }
    }

    @Test
    fun `1 layer generics`() {
        val base = type<String>()
        val same = type<String>()
        val different = type<Int>()

        assert(base == base) { "same instance should be equal" }
        assert(base == same) { "same underlying type should be equal" }
        assert(base != different) { "different underlying types should be different" }
    }

    @Test
    fun `2 layers generics`() {
        val base = type<HashSet<String>>()
        val same = type<HashSet<String>>()
        val different = type<HashSet<Int>>()

        assert(base == base) { "same instance should be equal" }
        assert(base == same) { "same underlying type should be equal" }
        assert(base != different) { "different underlying types should be different" }
    }

    @Test
    fun `mixed generics`() {
        val type1 = type<List<HashSet<String>>>()
        val type1copy = type<List<HashSet<String>>>()
        val type2 = type<HashSet<String>>()
        val type2copy = type<HashSet<String>>()
        val type3 = type<List<String>>()
        val type3copy = type<List<String>>()
        val type4 = type<String>()

        assert(type1 == type1copy) { "same underlying type should be equal" }
        assert(type2 == type2copy) { "same underlying type should be equal" }
        assert(type3 == type3copy) { "same underlying type should be equal" }

        assert(type1 != type2) { "different underlying types should be different" }
        assert(type1 != type3) { "different underlying types should be different" }
        assert(type2 != type3) { "different underlying types should be different" }

        assert(type1 != type4) { "different underlying types should be different" }
        assert(type2 != type4) { "different underlying types should be different" }
        assert(type3 != type4) { "different underlying types should be different" }
    }

    @Test
    fun `types from objects`() {
        val string = "test"
        val stringType = string.toType()
        val stringTypeCopy = string.toType()

        val number = 1
        val numberType = number.toType()
        val numberTypeCopy = number.toType()

        val list = emptyList<HashSet<String>>()
        val listType = list.toType()
        val listTypeCopy = list.toType()

        val set = emptySet<String>()
        val setType = set.toType()
        val setTypeCopy = set.toType()

        assert(stringType == stringTypeCopy) { "same underlying type should be equal" }
        assert(numberType == numberTypeCopy) { "same underlying type should be equal" }
        assert(listType == listTypeCopy) { "same underlying type should be equal" }
        assert(setType == setTypeCopy) { "same underlying type should be equal" }

        assert(stringType != numberType) { "different underlying types should be different" }
        assert(listType != setType) { "different underlying types should be different" }
        assert(listType != stringType) { "different underlying types should be different" }
        assert(setType != numberType) { "different underlying types should be different" }
    }
}