package sales;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	@Mock
	private SalesDao salesDao;
	@InjectMocks
	private SalesApp salesApp;

	@Test
	public void testGenerateReport_givenNullSalesId_thenGetSalesByIdNotBeCalled(){
		//given
		String salesId = null;
		salesApp = spy(new SalesApp());
		//when
		salesApp.generateSalesActivityReport(salesId,true);
		//then
		verify(salesApp,times(0)).getSalesById(salesId);
	}
}
