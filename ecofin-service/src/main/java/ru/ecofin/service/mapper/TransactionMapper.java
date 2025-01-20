package ru.ecofin.service.mapper;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;
import ru.ecofin.service.dto.response.HistoryResponseDto;
import ru.ecofin.service.entity.Transaction;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TransactionMapper {

  @Mappings({
      @Mapping(target = "transactionId", source = "transaction.id", qualifiedByName = "uuidToString"),
      @Mapping(target = "category", source = "transaction.category.id", qualifiedByName = "uuidToString"),
      @Mapping(target = "amount", source = "transaction.amount"),
      @Mapping(target = "transactionDate", source = "transaction.transactionDate")
  })
  HistoryResponseDto.Transaction transactionToDto(Transaction transaction);

  List<HistoryResponseDto.Transaction> transactionsToDtos(List<Transaction> transactions);

  @Named("uuidToString")
  default String uuidToString(UUID uuid) {
    return uuid != null ? uuid.toString() : null;
  }

  @Mappings({
      @Mapping(target = "transactionType", source = "dto.transactionType"),
      @Mapping(target = "amount", source = "dto.amount")
  })
  void transactionDtoToTransaction(CreateTransactionRequestDto dto,
      @MappingTarget Transaction transaction);
}
