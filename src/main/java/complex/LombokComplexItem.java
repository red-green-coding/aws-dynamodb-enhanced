package complex;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

// [START example]
@Value
@Builder
@DynamoDbImmutable(builder = LombokComplexItem.LombokComplexItemBuilder.class)
public class LombokComplexItem {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    String partitionKey;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    int sortKey;

    // Scalar Types – A scalar type can represent exactly one value.
    // The scalar types are number, string, binary, boolean, and null.

    String stringAttribute;

    Boolean booleanAttribute;
    boolean booleanPrimitiveAttribute;

    Integer intAttribute;

    Long longAttribute;

    Float floatAttribute;

    Double doubleAttribute;

    Short shortAttribute;

    byte[] byteAttribute;

    // Document Types – A document type can represent a complex structure
    // with nested attributes, such as what you would find in a JSON document.
    // The document types are list and map.
    List<String> stringList;

    Map<String, String> stringStringMap;

    // The document types are list and map. These data types can be
    // nested within each other, to represent complex data
    // structures up to 32 levels deep.

    List<Nested> nestedList;

    Map<String, Nested> nestedMap;

    List<NestedLombok> nestedLombokList;

    // Set Types – A set type can represent multiple scalar values.
    // The set types are string set, number set, and binary set.

    Set<String> stringSet;

    // when using @With dynamoDb needs to ignore it

    @With(onMethod_ = {@DynamoDbIgnore})
    String fieldUsingWith;
}
// [END example]
