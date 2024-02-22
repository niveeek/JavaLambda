package com.lambda;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BankRequest {
  private BigDecimal amount;
  private Integer term;
  private BigDecimal rate;
}
