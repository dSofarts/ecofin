package ru.ecofin.service.controller.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ecofin.service.annotation.ObjectId;
import ru.ecofin.service.dto.request.CreateCategoryRequestDto;
import ru.ecofin.service.dto.request.CreateTransactionRequestDto;
import ru.ecofin.service.dto.request.UpdateCategoryRequestDto;
import ru.ecofin.service.dto.response.AllCategoryResponseDto;
import ru.ecofin.service.dto.response.FrontResponseDto;
import ru.ecofin.service.dto.response.HistoryResponseDto;

@Tag(name = "API для управления категориями")
@RequestMapping(value = "category", produces = MediaType.APPLICATION_JSON_VALUE)
public interface CategoryControllerApi {

  @PostMapping("/create")
  ResponseEntity<FrontResponseDto> createCategory(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody CreateCategoryRequestDto requestBody);

  @PatchMapping("/update")
  ResponseEntity<FrontResponseDto> updateCategory(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody UpdateCategoryRequestDto requestBody);

  @DeleteMapping("/{categoryId}")
  ResponseEntity<FrontResponseDto> deleteCategory(
      @RequestHeader Map<String, String> requestHeader,
      @PathVariable("categoryId") @ObjectId String categoryId);

  @GetMapping("/category")
  ResponseEntity<AllCategoryResponseDto> getAllCategory(
      @RequestHeader Map<String, String> requestHeader);

  @GetMapping("/history/{walletId}")
  ResponseEntity<HistoryResponseDto> getHistory(
      @RequestHeader Map<String, String> requestHeader,
      @PathVariable("walletId") @ObjectId String walletId);

  @PostMapping("/transaction/create")
  ResponseEntity<FrontResponseDto> createTransaction(
      @RequestHeader Map<String, String> requestHeader,
      @RequestBody CreateTransactionRequestDto requestBody);
}
