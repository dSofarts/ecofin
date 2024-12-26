package ru.ecofin.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
