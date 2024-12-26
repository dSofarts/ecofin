package ru.ecofin.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
