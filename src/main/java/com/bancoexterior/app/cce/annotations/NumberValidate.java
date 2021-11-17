package com.bancoexterior.app.cce.annotations;


import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberValidate implements 
ConstraintValidator<ANumberValidate, Object> {

	   private BigDecimal min;
	   private BigDecimal max;
	   private int decimales;
	
  @Override
  public void initialize(ANumberValidate number) {
	this.min = BigDecimal.valueOf(number.min());
	this.max = BigDecimal.valueOf(number.max());
	
	this.decimales = number.decimales();
  }

  @Override
  public boolean isValid(final Object object, final ConstraintValidatorContext cvc) {
      boolean isValid = false;

      if (object == null) { 
          isValid = false;
      } else {
          BigDecimal bigDecimal = new BigDecimal(object.toString());
          isValid = (bigDecimal.compareTo(min) >= 0 ) && 
        		    (bigDecimal.compareTo(max) <= 0 ) && 
        		    (bigDecimal.scale() <= decimales);
      }
      
      return isValid;
  }

}
