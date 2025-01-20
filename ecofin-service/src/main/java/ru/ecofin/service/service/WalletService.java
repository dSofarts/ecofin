package ru.ecofin.service.service;

import ru.ecofin.service.dto.request.TransferRequest;
import ru.ecofin.service.dto.request.UserWalletRequestDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.UserWalletResponseDto;
import ru.ecofin.service.dto.response.WalletsResponseDto;

public interface WalletService {

  String createWallet(String userId);

  WalletsResponseDto findWallet(String userId);

  UserWalletResponseDto getUserWallet(UserWalletRequestDto requestDto);

  FrontResponseDto transfer(TransferRequest transferRequest);
}
