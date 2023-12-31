package basic;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

// [START example]
@Value
@Builder
@DynamoDbImmutable(builder = LombokImmutableItem.LombokImmutableItemBuilder.class)
public class LombokImmutableItem {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    String partitionKey;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    int sortKey;

    String stringAttribute;
}
// [END example]
