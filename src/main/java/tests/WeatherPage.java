package tests;


import databases.ReadDaysMaxMinDB;
import databases.UpdateWeatherDB;
import databases.WeatherMap;
import interfaces.GetInformationByZIPImpl;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.*;


import references.CityWeather;
import references.Days;


public class WeatherPage extends WebPage {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WeatherPage.class);


    public static String dateFromForm = "2018-02-12";
    public static String dateToForm = "2019-07-12";
    private static String zipcode;
    private static String date;


    private static Set<Item<CityWeather>> listItems = new HashSet<>();

    private WeatherMap weatherMap;


    static Map<Integer, CityWeather> cityWeatherMap = new TreeMap<>();


    public WeatherPage() {

        this.weatherMap = new WeatherMap();


        cityWeatherMap = weatherMap.getWeatherMap();


        final WebMarkupContainer content = new WebMarkupContainer("content");

        //Form 1

        Form<?> form = new Form("form");
        TextField<String> dateFromText = new TextField<>("dateFrom", new PropertyModel<String>(this, "dateFromForm"));
        TextField<String> dateToText = new TextField<>("dateTo", new PropertyModel<String>(this, "dateToForm"));


        Button button = new Button("filter") {
            @Override
            public void onSubmit() {
                super.onSubmit();

                log.info("filter button is pressed");
                log.info("DateFrom from form is: " + dateFromForm);
                log.info("DateTo from form is: " + dateToForm);



            }
        };
        Button reportbutton = new Button("reportbutton") {

            @Override
            public void onSubmit() {
                super.onSubmit();
                PageParameters pageParameters = new PageParameters();

                log.info("report button is pressed");

                pageParameters.add("datefrom", dateFromForm);
                pageParameters.add("dateto", dateToForm);

                setResponsePage(SuccessPage.class, pageParameters);

            }
        };










        dateFromText.setRequired(true);
        dateToText.setRequired(true);
        add(form);

        FeedbackPanel feedbackPanel1 = new FeedbackPanel("feedback");
        form.add(feedbackPanel1);

        form.add(dateFromText);
        form.add(dateToText);
        form.add(button);
        form.add(reportbutton);


//Form 2

        Form<?> form2 = new Form("form2");
        TextField<String> zipText = new TextField<>("zip", new PropertyModel<String>(this, "zipcode"));
        TextField<String> dateText = new TextField<>("date", new PropertyModel<String>(this, "date"));

        Button button2 = new Button("button") {
            @Override
            public void onSubmit() {
                super.onSubmit();

                log.info("Forecast button is pressed");


                GetInformationByZIPImpl getInformationByZIP = new GetInformationByZIPImpl();

                System.out.println(getInformationByZIP.SaveCityWeatherByZIPandDate(zipcode, date));

                cityWeatherMap = weatherMap.getWeatherMap();

            }
        };
        zipText.setRequired(true);
        dateText.setRequired(true);
        add(form2);

        form2.add(zipText);
        form2.add(dateText);
        form2.add(button2);


        content.setVisible((dateFromForm != null) & (dateToForm != null));


        add(content);

        final DataView<CityWeather> list = new DataView<CityWeather>("rows", new WeatherProvider()) {
            @Override
            protected void populateItem(final Item<CityWeather> item) {
                CityWeather cityWeather = item.getModelObject();

                item.add(new Label("data", cityWeather.getDateCity()));
                item.add(new MyEditableLable("city", new PropertyModel(cityWeather, "city"),item));

                item.add(new MyEditableLable("state", new PropertyModel(cityWeather, "state"),item));

                item.add(new MyEditableLable("weatherID", new PropertyModel(cityWeather, "weatherID"),item));
                item.add(new MyEditableLable("description", new PropertyModel(cityWeather, "description"),item));
                item.add(new MyEditableLable("temp", new PropertyModel(cityWeather, "temperature"),item));
                item.add(new MyEditableLable("relativeHumidity", new PropertyModel(cityWeather, "relativeHumidity"),item));
                item.add(new MyEditableLable("wind", new PropertyModel(cityWeather, "windSpeed"),item));
                item.add(new Label("windDirectionID", cityWeather.getWindDirection()));
            }
        };


        content.add(list);


        Form<?> form3 = new Form("form3");

        AjaxButton buttonS = new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);

                UpdateWeatherDB updateWeatherDB = new UpdateWeatherDB();
                updateWeatherDB.UpdateWeather(listItems);
                log.info("Update button is pressed");

            }

        };
        form3.add(buttonS);


        add(form3);

        final DataView<Days> list2 = new DataView("rows2", new DaysProvider()) {
            @Override
            protected void populateItem(Item item) {
                Days days = (Days) item.getModelObject();

                item.add(new Label("date2", days.getDate()));
                item.add(new Label("maxtemp", days.getMaxtemp()));
                item.add(new Label("mintemp", days.getMintemp()));
                item.add(new Label("avwind", days.getWind_average()));

            }
        };

        add(list2);


    }

    public static class WeatherProvider implements org.apache.wicket.markup.repeater.data.IDataProvider<CityWeather> {

        private List<CityWeather> filtered;

        private List<CityWeather> getWeather() {


            if (filtered == null) {
                filtered = filter();
            }
            return filtered;

        }

        public List<CityWeather> filter() {
            List<CityWeather> cityWeathers;
            WeatherMap listFiltered = new WeatherMap();
            cityWeathers = listFiltered.getWeatherListByDate(dateFromForm, dateToForm);

            return cityWeathers;
        }


        @Override
        public Iterator<? extends CityWeather> iterator(long l, long l1) {
            return getWeather().iterator();
        }

        @Override
        public long size() {
            return getWeather().size();
        }

        @Override
        public IModel<CityWeather> model(CityWeather cityWeather) {
            return new Model<>(cityWeather);
        }

        @Override
        public void detach() {
            filtered = null;

        }
    }

    public class DaysProvider implements org.apache.wicket.markup.repeater.data.IDataProvider<Days> {

        public List<Days> getDays() {
            List<Days> days;

            ReadDaysMaxMinDB readDaysMaxMinDB = new ReadDaysMaxMinDB();
            days = readDaysMaxMinDB.getStatistic();

            return days;
        }

        @Override
        public Iterator<? extends Days> iterator(long l, long l1) {
            return getDays().iterator();
        }

        @Override
        public long size() {
            return getDays().size();
        }

        @Override
        public IModel<Days> model(Days days) {
            return new Model<>(days);
        }

        @Override
        public void detach() {


        }
    }

    public class MyEditableLable extends AjaxEditableLabel {
        Item<CityWeather> item;
        public MyEditableLable(String id, IModel model,Item<CityWeather> item) {
            super(id, model);
            this.item=item;
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target) {
            super.onSubmit(target);
            listItems.add(item);
        }
    }


}
