package ru.ecofin.service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FrontResponseDto {

  @Schema(description = "Id объекта")
  private String objectId;
  @Schema(description = "HTTP код", example = "200")
  private String statusCode;
  @Schema(description = "Развернутое описание")
  private String status;
}
