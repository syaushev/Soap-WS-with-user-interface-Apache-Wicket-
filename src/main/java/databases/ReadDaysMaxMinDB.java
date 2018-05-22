package databases;


import references.Days;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadDaysMaxMinDB {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReadDaysMaxMinDB.class);
    public List<Days> getStatistic() {

        List<Days> daysList = new ArrayList<>();

        try (Connection conn = myDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT date,max(temp),min(temp),round(avg(id_dir)) FROM weather.public.weather_info GROUP BY date ORDER BY date;")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Days day = new Days();

                day.setDate(rs.getDate("date"));
                day.setMaxtemp(rs.getString("max"));
                day.setMintemp(rs.getString("min"));
                int directionID=Integer.parseInt(rs.getString("round"));
                if(directionID==1){
                    day.setWind_average("Северный");
                } else if(directionID==2){
                    day.setWind_average("Южный");
                }else if(directionID==3){
                    day.setWind_average("Западный");
                }else if(directionID==4){
                    day.setWind_average("Восточный");
                }

                daysList.add(day);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.warn("SQL exception (getStatistic method)");
        }

        return daysList;

    }
    public String getMaxAndMinTemp (){
        String temp=null;

        try (Connection conn = myDB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT max(temp) FROM weather_info;")){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                temp=rs.getString("max");


            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.warn("SQL exception (getMaxtemp method)");
        }

        return temp;

    }

}
