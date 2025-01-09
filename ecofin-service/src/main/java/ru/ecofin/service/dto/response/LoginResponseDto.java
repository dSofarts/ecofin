package ru.ecofin.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ecofin.service.utils.time.DateToStringConverter;
import ru.ecofin.service.utils.time.TimeDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDto {

  @Schema(description = "Идентификатор операции", example = "e13e447b-f740-4231-904e-393bf288eba9")
  private String operationId;
  @Schema(description = "Срок действия OTP")
  @JsonDeserialize(using = TimeDeserializer.class)
  @JsonSerialize(converter = DateToStringConverter.class)
  private ZonedDateTime expired;
  @Schema(description = "Временный токен безопасности")
  private String tempToken;
}
