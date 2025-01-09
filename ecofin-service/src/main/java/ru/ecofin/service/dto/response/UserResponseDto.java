package ru.ecofin.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {
  @Schema(description = "ID пользователя", example = "e13e447b-f740-4231-904e-393bf288eba9")
  private String id;
  @Schema(description = "Телефон пользователя", example = "79999999999")
  private String phone;
  @Schema(description = "Данные пользователя")
  private UserInfo userInfo;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class UserInfo {
    @Schema(description = "Фамилия пользователя")
    private String lastName;
    @Schema(description = "Имя пользователя")
    private String firstName;
    @Schema(description = "Отчество пользователя (при наличии)")
    private String middleName;
    @Schema(description = "Дата рождения пользователя")
    private LocalDate birthDate;
  }
}
