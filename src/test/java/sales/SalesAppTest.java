package sales;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	@Mock
	private SalesDao salesDao;
	@Mock
	private SalesReportDao salesReportDao;
	@Mock
	private EcmService ecmService;
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

	@Test
	public void testGenerateReport_givenSalesId_thenGiveSales(){
		//given
		String salesId = "S100";
		salesApp = spy(new SalesApp());
		Sales sales = mock(Sales.class);
		doReturn(sales).when(salesApp).getSalesById(salesId);
		doReturn(false).when(salesApp).isSalesEffective(sales);
		//when
		salesApp.generateSalesActivityReport(salesId,true);
		//then
		verify(salesApp,times(1)).getSalesById(salesId);
	}

	@Test
	public void testGenerateReport_givenSalesIdAndSalesEffective_thenGiveReportDataList(){
		//given
		String salesId = "S100";
		salesApp = spy(new SalesApp());
		Sales sales = mock(Sales.class);
		doReturn(sales).when(salesApp).getSalesById(salesId);
		doReturn(new Date()).when(sales).getEffectiveFrom();
		doReturn(new Date()).when(sales).getEffectiveTo();
		Date today = mock(Date.class);
		when(today.after(any())).thenReturn(true);
		when(today.before(any())).thenReturn(true);
		SalesActivityReport report = mock(SalesActivityReport.class);
		doReturn(report).when(salesApp).generateReport(anyList(),anyList());
		//when
		salesApp.generateSalesActivityReport(salesId,true);
		//then
		verify(salesApp,times(1)).getSalesReportData(sales);
	}

	@Test
	public void testGenerateReport_givenSalesIdAndSalesEffective_thenGiveNoReportDataList(){
		//given
		String salesId = "S100";
		salesApp = spy(new SalesApp());
		Sales sales = mock(Sales.class);
		doReturn(sales).when(salesApp).getSalesById(salesId);
		doReturn(getYesterDay()).when(sales).getEffectiveFrom();
		doReturn(getTomorrow()).when(sales).getEffectiveTo();
		//when
		salesApp.generateSalesActivityReport(salesId,true);
		//then
		verify(salesApp,times(0)).getSalesReportData(sales);
	}

	@Test
	public void testGenerateReport_givenSalesIdAndNatTradeIsTrue_thenGiveHeaders(){
		//given
		String salesId = "S100";
		salesApp = spy(new SalesApp());
		Sales sales = mock(Sales.class);
		doReturn(sales).when(salesApp).getSalesById(salesId);
		doReturn(new Date()).when(sales).getEffectiveFrom();
		doReturn(new Date()).when(sales).getEffectiveTo();
		boolean isNatTrade = true;
		SalesActivityReport report = mock(SalesActivityReport.class);
		doReturn(report).when(salesApp).generateReport(anyList(),anyList());
		//when
		salesApp.generateSalesActivityReport(salesId,isNatTrade);
		//then
		verify(salesApp,times(1)).getHeaders(isNatTrade);
	}

	@Test
	public void testUploadDocument_givenAReport_thentoXmlBeCalled(){
		//given
		salesApp = spy(new SalesApp());
		SalesActivityReport report = mock(SalesActivityReport.class);
		//when
		salesApp.uploadDocument(report);
		//then
		verify(report,times(1)).toXml();
	}

	private Date getYesterDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,-1);
		return calendar.getTime();
	}

	private Date getTomorrow(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE,1);
		return calendar.getTime();
	}


}
