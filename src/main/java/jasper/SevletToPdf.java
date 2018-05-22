package jasper;

import databases.ReadDaysMaxMinDB;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import references.Days;
import tests.WeatherPage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class SevletToPdf extends HttpServlet {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SevletToPdf.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ReadDaysMaxMinDB readDaysMaxMinDB = new ReadDaysMaxMinDB();
        List<Days> itemList=readDaysMaxMinDB.getStatistic();

        Date date1=itemList.get(1).getDate();
        Date date2=itemList.get(itemList.size()-1).getDate();

        String tempmax=readDaysMaxMinDB.getMaxAndMinTemp();




        JRBeanCollectionDataSource jrBeanCollectionDataSource=new JRBeanCollectionDataSource(itemList);

        Map<String,Object> map=new HashMap<>();
        map.put("ItemDS",jrBeanCollectionDataSource);
        map.put("date1",date1);
        map.put("date2",date2);
        map.put("maxtemp",tempmax);




        JasperReport jasperReport= null;
        try {
            jasperReport = JasperCompileManager.compileReport(req.getServletContext().getRealPath("/WEB-INF/")+"reportWeek.jrxml");



            byte[] bytestream= JasperRunManager.runReportToPdf(jasperReport, map, jrBeanCollectionDataSource);
            OutputStream outputStream=resp.getOutputStream();
            resp.setContentType("application/pdf");
            resp.setContentLength(bytestream.length);
            outputStream.write(bytestream,0,bytestream.length);


        } catch (JRException e) {
            e.printStackTrace();
            log.warn("JR Eception");
        }
    }
}
