package jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import references.CityWeather;
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

public class SevletToDoc2 extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SevletToDoc2.class);
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
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);
            OutputStream outputStream=resp.getOutputStream();
            resp.setContentType("application/msword");
            JRDocxExporter jrDocxExporter=new JRDocxExporter();
            jrDocxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            jrDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            jrDocxExporter.exportReport();


        } catch (JRException e) {
            e.printStackTrace();
            log.warn("JR exception");
        }
    }
}
