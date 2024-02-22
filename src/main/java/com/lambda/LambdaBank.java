package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * LambdaBank
 */
public class LambdaBank implements RequestHandler<BankRequest, BankResponse> {
  /**
   * handleRequest
   * @param bankRequest handleRequest
   * @param context context
   * @return
   */
  @Override
  public BankResponse handleRequest(BankRequest bankRequest, Context context) {
    MathContext mathContext = MathContext.DECIMAL128;
    BigDecimal amount = bankRequest.getAmount().
        setScale(2, RoundingMode.HALF_UP);
    BigDecimal monthlyRate = bankRequest.getRate().
        setScale(2, RoundingMode .HALF_UP).
        divide(BigDecimal.valueOf(100), mathContext);
    BigDecimal mothlyRateWithAccount = bankRequest.getRate().
        subtract(BigDecimal.valueOf(0.2), mathContext).
        setScale(2, RoundingMode.HALF_UP).
        divide(BigDecimal.valueOf(100), mathContext);
    Integer term = bankRequest.getTerm();
    BigDecimal monthlyPayment = this.calculateQuota(amount, monthlyRate, term, mathContext);
    BigDecimal monthlyPaymentWithAccount = this.calculateQuota(amount, mothlyRateWithAccount, term, mathContext);
    //INSTANCE
    BankResponse bankResponse = new BankResponse();
    bankResponse.setQuota(monthlyPayment);
    bankResponse.setRate(monthlyRate);
    bankResponse.setTerm(term);
    bankResponse.setQuotaWithAccount(monthlyPaymentWithAccount);
    bankResponse.setRateWithAccount(mothlyRateWithAccount);
    bankResponse.setTermWithAccount(term);
    return bankResponse;
  }

  /**
   * calculateQuota
   * @param amount amount
   * @param rate rate
   * @param term term
   * @param mathContext mathContext
   * @return
   */
  public BigDecimal calculateQuota(BigDecimal amount, BigDecimal rate, Integer term, MathContext mathContext) {
    BigDecimal onePlusRate = rate.add(BigDecimal.ONE, mathContext);
    BigDecimal onePlusRateToN = onePlusRate.pow(term, mathContext);
    BigDecimal onePlusRateToNNegative = BigDecimal.ONE.divide(onePlusRateToN, mathContext);
    BigDecimal numerator = amount.multiply(rate, mathContext);
    BigDecimal denominator = BigDecimal.ONE.subtract(onePlusRateToNNegative, mathContext);
    BigDecimal mothlyPayment = numerator.divide(denominator, mathContext);
    mothlyPayment.setScale(2, RoundingMode.HALF_UP);
    return mothlyPayment;
  }

  /**
   * P = Monto del préstamo
   * i = Tasa de interés mensual
   * n = plazo del crédito en meses
   * Cuota Mensual = (P * i)/(1 - (1 + i)^(-n))
   */
}
