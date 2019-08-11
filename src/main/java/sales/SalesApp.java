package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	private SalesReportDao salesReportDao = new SalesReportDao();
	private SalesDao salesDao = new SalesDao();
	public void generateSalesActivityReport(String salesId, boolean isNatTrade) {

		if (salesId == null) {
			return;
		}

		Sales sales = getSalesById(salesId);

		if (!isSalesEffective(sales)) return;

		List<SalesReportData> reportDataList = getSalesReportData(sales);

		List<String> headers = getHeaders(isNatTrade);

		SalesActivityReport report = this.generateReport(headers, reportDataList);

		uploadDocument(report);

	}

	protected void uploadDocument(SalesActivityReport report) {
		EcmService ecmService = new EcmService();
		ecmService.uploadDocument(report.toXml());
	}

	protected List<String> getHeaders(boolean isNatTrade) {
		List<String> headers = null;
		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}
		return headers;
	}

	protected boolean isSalesEffective(Sales sales) {
		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return true;
		}
		return false;
	}

	protected List<SalesReportData> getSalesReportData(Sales sales) {
		return this.salesReportDao.getReportData(sales);
	}

	protected Sales getSalesById(String salesId) {
		return this.salesDao.getSalesBySalesId(salesId);
	}

	protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
