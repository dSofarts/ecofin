package ru.ecofin.service.service;

import ru.ecofin.service.dto.response.WalletsResponseDto;

public interface WalletService {

  String createWallet(String userId);

  WalletsResponseDto findWallet(String userId);
}
