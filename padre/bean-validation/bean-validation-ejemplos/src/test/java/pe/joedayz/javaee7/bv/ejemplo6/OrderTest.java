package pe.joedayz.javaee7.bv.ejemplo6;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class OrderTest {

	 // ======================================
	  // =             Attributes             =
	  // ======================================

	  protected static ValidatorFactory vf;
	  protected static Validator validator;

	  private static Date creationDate;
	  private static Date paymentDate;
	  private static Date deliveryDate;


	  // ======================================
	  // =          Lifecycle Methods         =
	  // ======================================

	  @BeforeClass
	  public static void init() throws ParseException {
	    vf = Validation.buildDefaultValidatorFactory();
	    validator = vf.getValidator();

	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	    creationDate = dateFormat.parse("01/01/2010");
	    paymentDate = dateFormat.parse("02/01/2010");
	    deliveryDate = dateFormat.parse("03/01/2010");
	  }

	  @AfterClass
	  public static void close() {
	    vf.close();
	  }

	  // ======================================
	  // =              Methods               =
	  // ======================================

	  @Test
	  public void shouldRaiseNoConstraintsViolation() {

	    Order order = new Order();
	    order.setOrderId("CA45678");
	    order.setTotalAmount(1234.5);

	    order.setCreationDate(creationDate);
	    order.setPaymentDate(paymentDate);
	    order.setDeliveryDate(deliveryDate);

	    Set<ConstraintViolation<Order>> violations = validator.validate(order);
	    assertEquals(0, violations.size());
	  }

	  @Test
	  public void shouldRaiseConstraintsViolationCauseDatesAreNotChronological() {

	    Order order = new Order();
	    order.setOrderId("MN345678");
	    order.setTotalAmount(1234.5);

	    order.setCreationDate(deliveryDate);
	    order.setPaymentDate(paymentDate);
	    order.setDeliveryDate(deliveryDate);

	    Set<ConstraintViolation<Order>> violations = validator.validate(order);
	    displayContraintViolations(violations);
	    assertEquals(1, violations.size());
	  }

	  private void displayContraintViolations(Set<ConstraintViolation<Order>> constraintViolations) {
	    for (ConstraintViolation constraintViolation : constraintViolations) {
	      System.out.println("### " + constraintViolation.getRootBeanClass().getSimpleName() +
	              "." + constraintViolation.getPropertyPath() + " - Invalid Value = " + constraintViolation.getInvalidValue() + " - Error Msg = " + constraintViolation.getMessage());

	    }
	  }
}