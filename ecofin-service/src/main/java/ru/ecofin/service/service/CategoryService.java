package ru.ecofin.service.service;

import ru.ecofin.service.dto.request.CreateCategoryRequestDto;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;
import ru.ecofin.service.dto.request.UpdateCategoryRequestDto;
import ru.ecofin.service.dto.response.AllCategoryResponseDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.HistoryResponseDto;

public interface CategoryService {

  FrontResponseDto createCategory(CreateCategoryRequestDto requestBody, String phoneFromHeader);

  FrontResponseDto updateCategory(UpdateCategoryRequestDto requestBody, String phoneFromHeader);

  FrontResponseDto deleteCategory(String categoryId, String phoneFromHeader);

  AllCategoryResponseDto getAllCategory(String phoneFromHeader);

  HistoryResponseDto getHistory(String phoneFromHeader, String walletId);

  FrontResponseDto createTransaction(CreateTransactionRequestDto requestBody, String phoneFromHeader);
}
