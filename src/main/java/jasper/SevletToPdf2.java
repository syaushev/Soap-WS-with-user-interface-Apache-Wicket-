package jasper;

import databases.ReadDaysMaxMinDB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import references.CityWeather;
import references.Days;
import tests.WeatherPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SevletToPdf2 extends HttpServlet {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SevletToPdf2.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        WeatherPage.WeatherProvider weatherProvider=new WeatherPage.WeatherProvider();
        List<CityWeather> itemList=weatherProvider.filter();

        Date date1=itemList.get(1).getDateCity();
        Date date2=itemList.get(itemList.size()-1).getDateCity();








//
        JRBeanCollectionDataSource jrBeanCollectionDataSource=new JRBeanCollectionDataSource(itemList);

        Map<String,Object> map=new HashMap<>();
        map.put("ItemDS2",jrBeanCollectionDataSource);
        map.put("date1",date1);
        map.put("date2",date2);



        JasperReport jasperReport= null;
        try {
            jasperReport = JasperCompileManager.compileReport(req.getServletContext().getRealPath("/WEB-INF/")+"report2.jrxml");



            byte[] bytestream= JasperRunManager.runReportToPdf(jasperReport, map, jrBeanCollectionDataSource);
            OutputStream outputStream=resp.getOutputStream();
            resp.setContentType("application/pdf");
            resp.setContentLength(bytestream.length);
            outputStream.write(bytestream,0,bytestream.length);


        } catch (JRException e) {
            e.printStackTrace();
            log.warn("Jr exception");
        }
    }
}
