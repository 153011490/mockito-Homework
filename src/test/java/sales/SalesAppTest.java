package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	public void testGetSalesById_givenSalesId_thenGiveSalses(){
		//given
		String salesId = "S100";
		Sales sales = new Sales();
		when(salesDao.getSalesBySalesId(salesId)).thenReturn(sales);
		//when
		Sales result = salesApp.getSalesById(salesId);
		//then
		Assert.assertEquals(sales,result);
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

	@Test
	public void testGetHeaders_givenNatTradeTrue_thenReturnHeaderHasTime() {
		//given
		salesApp = spy(new SalesApp());
		boolean isNatTrade=true;
		//when
		List<String> headers=salesApp.getHeaders(isNatTrade);
		Assert.assertEquals("Time",headers.get(3));
	}

	@Test
	public void testGetHeaders_givenNatTradeFalse_thenReturnHeaderHasLocalTime() {
		//given
		salesApp = spy(new SalesApp());
		boolean isNatTrade=false;
		//when
		List<String> headers=salesApp.getHeaders(isNatTrade);
		Assert.assertEquals("Local Time",headers.get(3));
	}

	@Test
	public void testIsSalesEffective_givenSalesAndNotEffective_thenReturnFalse() {
		//given
		salesApp = spy(new SalesApp());
		Sales sales = mock(Sales.class);
		doReturn(getYesterDay()).when(sales).getEffectiveFrom();
		doReturn(getTomorrow()).when(sales).getEffectiveTo();
		//when
		boolean result = salesApp.isSalesEffective(sales);
		Assert.assertEquals(false,result);
	}

	@Test
	public void testGetSalesReportData_givenEffectiveSales_thenReturnSalesReportDataList() {
		//given
		Sales sales=new Sales();
		List<SalesReportData> salesReportDataList=new ArrayList<>();
		salesReportDao=mock(SalesReportDao.class);
		when(salesReportDao.getReportData(sales)).thenReturn(salesReportDataList);
		//when
		List<SalesReportData> result = salesApp.getSalesReportData(sales);
		//then
		Assert.assertEquals(salesReportDataList,result);
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
