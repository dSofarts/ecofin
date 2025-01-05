package ru.ecofin.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrontResponseDto {

  @Schema(description = "Id объекта", example = "e13e447b-f740-4231-904e-393bf288eba9")
  private String objectId;
  @Schema(description = "HTTP код", example = "200")
  private String statusCode;
  @Schema(description = "Развернутое описание", example = "Transfer successfully completed")
  private String status;
}
