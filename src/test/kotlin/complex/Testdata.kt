package complex

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.choose
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.lazy
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid

private val aShortString = Arb.string(1..10)
private val anEmptyOrShortString = Arb.string(0..10)

// partition key may not be empty
private val aPartitionKey = Arb.uuid().map { it.toString() }

private val aSortKey = Arb.int()

// [START example]
private fun nested(level: Int): Arb<Nested> =
    arbitrary {
        Nested().apply {
            stringAttribute = aShortString.bind()
            nestedList =
                if (level == 0 || Arb.choose(1 to true, 4 to false).bind()) {
                    // recursion is limited, additionally limit depth in 20% of cases
                    emptyList()
                } else {
                    Arb.lazy { Arb.list(nested(level - 1), 0..3) }.bind()
                }
        }
    }

private fun nestedLombok(level: Int): Arb<NestedLombok> =
    arbitrary {
        NestedLombok.builder()
            .stringAttribute(aShortString.bind())
            .nestedList(
                if (level == 0 || Arb.choose(1 to true, 4 to false).bind()) {
                    // recursion is limited, additionally limit depth in 20% of cases
                    emptyList()
                } else {
                    Arb.lazy { Arb.list(nestedLombok(level - 1), 0..3) }.bind()
                },
            )
            .build()
    }

val collectionMaxSize = 5

val aLombokComplexItem =
    arbitrary {
        LombokComplexItem.builder()
            .partitionKey(aPartitionKey.bind())
            .sortKey(aSortKey.bind())
            // scalars
            .stringAttribute(aShortString.orNull().bind())
            .intAttribute(Arb.int().orNull().bind())
            .booleanAttribute(Arb.boolean().orNull().bind())
            .booleanPrimitiveAttribute(Arb.boolean().bind())
            .longAttribute(Arb.long().orNull().bind())
            // Numbers can have up to 38 digits of precision.
            // Exceeding this results in an exception.
            // If you need greater precision than 38 digits, you can use strings.
            // the range we are using is not completely accurate, supported range is prohably bigger
            .doubleAttribute(Arb.double(includeNonFiniteEdgeCases = false, range = -10e120..10e120).orNull().bind())
            // default mapper cannot handle Infinity and NaN
            // -0.0f is mapped back to 0.0 so we need to filter it out
            .floatAttribute(
                Arb.float(includeNonFiniteEdgeCases = false)
                    .filter { it != -0.0f }.orNull().bind(),
            )
            .shortAttribute(Arb.short().orNull().bind())
            .byteAttribute(Arb.byteArray(Arb.int(0..10), Arb.byte()).bind())
            // lists may be empty
            .stringList(Arb.list(anEmptyOrShortString, 0..collectionMaxSize).bind())
            // map may be empty, map keys may not be empty
            .stringStringMap(Arb.map(aShortString, anEmptyOrShortString, maxSize = collectionMaxSize).bind())
            // set may not be empty, entries may be empty
            .stringSet(Arb.set(anEmptyOrShortString, 1..collectionMaxSize).bind())
            // lists may be empty
            .nestedList(Arb.list(nested(3), 0..collectionMaxSize).bind())
            .nestedLombokList(Arb.list(nestedLombok(3), 0..collectionMaxSize).bind())
            // map keys may not be empty
            .nestedMap(Arb.map(aShortString, nested(3), maxSize = collectionMaxSize).bind())
            .build()
    }
// [END example]

val aJavaComplexItem =
    arbitrary {
        JavaComplexItem().apply {
            partitionKey = aPartitionKey.bind()
            sortKey = aSortKey.bind()

            // attributes may be null
            stringAttribute = aShortString.orNull().bind()

            nestedList = Arb.list(nested(3), 0..collectionMaxSize).bind()
        }
    }
