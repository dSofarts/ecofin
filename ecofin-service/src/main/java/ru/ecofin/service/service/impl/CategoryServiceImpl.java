package ru.ecofin.service.service.impl;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecofin.service.dto.request.CreateCategoryRequestDto;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;
import ru.ecofin.service.dto.request.UpdateCategoryRequestDto;
import ru.ecofin.service.dto.response.AllCategoryResponseDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.HistoryResponseDto;
import ru.ecofin.service.entity.Category;
import ru.ecofin.service.entity.Transaction;
import ru.ecofin.service.entity.User;
import ru.ecofin.service.entity.Wallet;
import ru.ecofin.service.entity.enums.TransactionType;
import ru.ecofin.service.exception.BadRequestException;
import ru.ecofin.service.exception.NotFoundException;
import ru.ecofin.service.mapper.TransactionMapper;
import ru.ecofin.service.repository.CategoryRepository;
import ru.ecofin.service.repository.TransactionRepository;
import ru.ecofin.service.service.CategoryService;
import ru.ecofin.service.service.UserService;
import ru.ecofin.service.utils.RestUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final UserService userService;
  private final CategoryRepository categoryRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  @Override
  @Transactional
  public FrontResponseDto createCategory(CreateCategoryRequestDto requestBody,
      String phoneFromHeader) {
    log.info("Create category processing started");
    User user = userService.getUserByPhone(phoneFromHeader);
    Category category = Category.builder()
        .categoryName(requestBody.getCategoryName())
        .user(user).build();
    if (requestBody.getLimit() != null) {
      log.info("Set category limit");
      category.setBudgetLimit(requestBody.getLimit());
    }
    category = categoryRepository.save(category);
    return FrontResponseDto.builder()
        .objectId(category.getId().toString())
        .status("Category successfully created")
        .statusCode("200").build();
  }

  @Override
  @Transactional
  public FrontResponseDto updateCategory(UpdateCategoryRequestDto requestBody,
      String phoneFromHeader) {
    log.info("Update category processing started");
    User user = userService.getUserByPhone(phoneFromHeader);
    Category category = user.getCategories().stream().filter(cat -> cat.getId().toString()
        .equals(requestBody.getCategoryId())).findFirst().orElseThrow(
        () -> new NotFoundException(requestBody.getCategoryId(), "Category not found"));
    if (requestBody.getLimit() != null) {
      log.info("Update category limit");
      category.setBudgetLimit(requestBody.getLimit());
    }
    if (requestBody.getCategoryName() != null) {
      log.info("Update category name");
      category.setCategoryName(requestBody.getCategoryName());
    }

    return FrontResponseDto.builder()
        .objectId(requestBody.getCategoryId())
        .statusCode("200")
        .status("Category successfully updated").build();
  }

  @Override
  @Transactional
  public FrontResponseDto deleteCategory(String categoryId, String phoneFromHeader) {
    log.info("Delete category processing started");
    User user = userService.getUserByPhone(phoneFromHeader);
    Category category = user.getCategories().stream().filter(cat -> cat.getId().toString()
        .equals(categoryId)).findFirst().orElseThrow(
        () -> new NotFoundException(categoryId, "Category not found"));
    categoryRepository.delete(category);
    return FrontResponseDto.builder()
        .objectId(categoryId)
        .status("Category successfully deleted")
        .statusCode("200").build();
  }

  @Override
  @Transactional(readOnly = true)
  public AllCategoryResponseDto getAllCategory(String phoneFromHeader) {
    User user = userService.getUserByPhone(phoneFromHeader);
    List<AllCategoryResponseDto.Category> categoryList = new ArrayList<>();

    user.getCategories().forEach(cat -> {
      BigDecimal amount = findAmountForCategoryInThisMount(cat);
      categoryList.add(AllCategoryResponseDto.Category.builder()
          .name(cat.getCategoryName())
          .categoryId(cat.getId().toString())
          .limit(cat.getBudgetLimit())
          .amountInThisMount(amount)
          .balanceInThisMount(cat.getBudgetLimit().subtract(amount))
          .build());

    });

    return AllCategoryResponseDto.builder()
        .userId(user.getId().toString())
        .categories(categoryList)
        .build();
  }

  private BigDecimal findAmountForCategoryInThisMount(Category cat) {
    return transactionRepository.getTransactionsByCategory(cat).stream()
        .filter(transaction -> transaction
            .getTransactionDate().getMonth().equals(ZonedDateTime.now().getMonth()))
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  @Transactional(readOnly = true)
  public HistoryResponseDto getHistory(String phoneFromHeader, String walletId) {
    User user = userService.getUserByPhone(phoneFromHeader);
    Wallet wallet = user.getWallets().stream().filter(wall ->
        walletId.equals(wall.getId().toString())).findFirst().orElseThrow(
        () -> new NotFoundException(walletId, "Wallet not found"));

    List<Transaction> transactionList = transactionRepository
        .getTransactionsByWalletId(wallet.getId());
    List<Transaction> incomeTransactionList = transactionList.stream().filter(transaction ->
        transaction.getTransactionType() == TransactionType.INCOME).toList();
    List<Transaction> expenseTransactionList = transactionList.stream().filter(transaction ->
        transaction.getTransactionType() == TransactionType.EXPENSE).toList();

    return HistoryResponseDto.builder()
        .userId(user.getId().toString())
        .wallet(wallet.getId().toString())
        .incomeTransactions(transactionMapper.transactionsToDtos(incomeTransactionList))
        .expenseTransactions(transactionMapper.transactionsToDtos(expenseTransactionList))
        .sumIncome(getSumFromTransactionList(incomeTransactionList))
        .sumExpense(getSumFromTransactionList(expenseTransactionList))
        .build();
  }

  private BigDecimal getSumFromTransactionList(List<Transaction> transactionList) {
    return transactionList.stream()
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  @Override
  @Transactional
  public FrontResponseDto createTransaction(CreateTransactionRequestDto requestBody,
      String phoneFromHeader) {
    User user = userService.getUserByPhone(phoneFromHeader);
    Wallet wallet = user.getWallets().stream().filter(wal -> wal.getId()
        .toString().equals(requestBody.getWalletId())).findFirst().orElseThrow(
        () -> new NotFoundException(requestBody.getWalletId(), "Wallet not found"));

    if (requestBody.getTransactionType() == TransactionType.EXPENSE
        && requestBody.getAmount().compareTo(wallet.getBalance()) > 0) {
      throw new BadRequestException("That amount is not in this wallet");
    }

    Category category = user.getCategories().stream().filter(cat -> cat.getId()
        .toString().equals(requestBody.getCategoryId())).findFirst().orElseThrow(
        () -> new NotFoundException(requestBody.getCategoryId(), "Category not found"));

    Transaction transaction = Transaction.builder()
        .category(category)
        .wallet(wallet).build();
    transactionMapper.transactionDtoToTransaction(requestBody, transaction);

    if (transaction.getTransactionType() == TransactionType.INCOME) {
      wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
    } else if (transaction.getTransactionType() == TransactionType.EXPENSE) {
      wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
    }

    transactionRepository.save(transaction);
    return RestUtils.buildResponseDto(transaction.getId().toString(),
        "Transaction added successfully",
        "200");
  }
}
