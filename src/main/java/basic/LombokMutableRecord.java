package basic;

import lombok.Data;
import lombok.Getter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
public class LombokMutableRecord {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    String partitionKey;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    int sortKey;

    String stringAttribute;
}
