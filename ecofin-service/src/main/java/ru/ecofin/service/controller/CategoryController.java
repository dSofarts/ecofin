package ru.ecofin.service.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.ecofin.service.annotation.LoggingUsed;
import ru.ecofin.service.controller.api.CategoryControllerApi;
import ru.ecofin.service.controller.validator.CreateCategoryRequestValidator;
import ru.ecofin.service.controller.validator.CreateTransactionRequestValidator;
import ru.ecofin.service.controller.validator.MainValidator;
import ru.ecofin.service.controller.validator.UpdateCategoryRequestValidator;
import ru.ecofin.service.dto.request.CreateCategoryRequestDto;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;
import ru.ecofin.service.dto.request.UpdateCategoryRequestDto;
import ru.ecofin.service.dto.response.AllCategoryResponseDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.HistoryResponseDto;
import ru.ecofin.service.security.JWTService;
import ru.ecofin.service.service.CategoryService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerApi {

  private final CategoryService categoryService;
  private final MainValidator mainValidator;
  private final JWTService jwtService;

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /category/create")
  public ResponseEntity<FrontResponseDto> createCategory(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody CreateCategoryRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, CreateCategoryRequestValidator.class, requestHeader);
    return ResponseEntity.ok(categoryService.createCategory(requestBody,
        jwtService.getPhoneFromHeader(requestHeader)));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "PATCH /category/update")
  public ResponseEntity<FrontResponseDto> updateCategory(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody UpdateCategoryRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, UpdateCategoryRequestValidator.class, requestHeader);
    return ResponseEntity.ok(categoryService.updateCategory(requestBody,
        jwtService.getPhoneFromHeader(requestHeader)));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "DELETE /category/{}")
  public ResponseEntity<FrontResponseDto> deleteCategory(
      @RequestHeader Map<String, String> requestHeader,
      String categoryId) {
    mainValidator.validateRequest(requestHeader);
    return ResponseEntity.ok(categoryService.deleteCategory(categoryId,
        jwtService.getPhoneFromHeader(requestHeader)));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /category/category")
  public ResponseEntity<AllCategoryResponseDto> getAllCategory(
      @RequestHeader Map<String, String> requestHeader) {
    mainValidator.validateRequest(requestHeader);
    return ResponseEntity.ok(categoryService.getAllCategory(
        jwtService.getPhoneFromHeader(requestHeader)));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "GET /category/history/{}")
  public ResponseEntity<HistoryResponseDto> getHistory(
      @RequestHeader Map<String, String> requestHeader, String walletId) {
    mainValidator.validateRequest(requestHeader);
    return ResponseEntity.ok(categoryService.getHistory(
        jwtService.getPhoneFromHeader(requestHeader), walletId));
  }

  @Override
  @SneakyThrows
  @LoggingUsed(endpoint = "POST /category/transaction/create")
  public ResponseEntity<FrontResponseDto> createTransaction(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody CreateTransactionRequestDto requestBody) {
    mainValidator.validateRequest(requestBody, CreateTransactionRequestValidator.class, requestHeader);
    return ResponseEntity.ok(categoryService.createTransaction(
        requestBody, jwtService.getPhoneFromHeader(requestHeader)));
  }
}
