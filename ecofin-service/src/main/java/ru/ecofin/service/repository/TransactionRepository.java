package ru.ecofin.service.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Category;
import ru.ecofin.service.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  List<Transaction> getTransactionsByWalletId(UUID walletId);

  List<Transaction> getTransactionsByCategory(Category category);
}
