package ru.ecofin.utils.masking.properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "masking")
public class MaskingProperties {

  @Getter
  protected char maskSymbol = '*';
  protected String[] notMaskingKeys = new String[]{"id", "error", "status", "operationId",
      "service-name"};
  protected String[] secretFields = new String[]{"password", "otp"};
  @Getter
  protected int maxNestedDepth = -1;
  protected double showFraction = 0.15d;
  @Getter
  protected Integer maxShowedChars = null;
  @Getter
  protected boolean showReaValueLength = false;
  @Getter
  protected boolean useMaskWithFraction = false;
  @Getter
  protected boolean showBooleanFields = false;

  public double getShowFraction() {
    if (showFraction <= 1d && showFraction >= 0d) {
      return this.showFraction;
    }
    return 0.1d;
  }

  public List<String> getNotMaskingKeys() {
    if (notMaskingKeys != null) {
      return Arrays.asList(notMaskingKeys);
    }
    return Collections.emptyList();
  }

  public List<String> getSecretFields() {
    if (secretFields != null) {
      return Arrays.asList(secretFields);
    }
    return Collections.emptyList();
  }
}
