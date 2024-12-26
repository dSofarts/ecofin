package ru.ecofin.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {

}
