package com.lambda;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString public class BankResponse {
  private BigDecimal quota;
  private BigDecimal rate;
  private Integer term;
  private BigDecimal quotaWithAccount;
  private BigDecimal rateWithAccount;
  private Integer termWithAccount;
}
