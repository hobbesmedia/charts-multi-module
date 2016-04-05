package com.example;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.LegendItemClickEvent;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.servlet.annotation.WebServlet;
import java.util.Collection;
import java.util.List;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.example.MyAppWidgetset")
public class MyUI extends UI {

    private final ListSeries bella = new ListSeries("Duna Bella", 42.4, 33.2,
            34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1);
    private final ListSeries legenda = new ListSeries("Dunai Legenda", 48.9, 38.8,
            39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2);
    private final ListSeries vacsora = new ListSeries("Vacsora hajó", 83.6, 78.8,
            98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3);
    private Chart chart;
    private OptionGroup optionGroup;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout demoCharts = new VerticalLayout();
        demoCharts.setSpacing(true);

        chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Total fruit consumption, grouped by gender");
        conf.setSubTitle("Source: WorldClimate.com");

        XAxis x = new XAxis();
        x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Rainfall (mm)");
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setBackgroundColor(new SolidColor("#FFFFFF"));
        legend.setAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(100);
        legend.setY(70);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.x +': '+ this.y +' mm'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        conf.addSeries(bella);
        conf.addSeries(legenda);
        conf.addSeries(vacsora);

        chart.addLegendItemClickListener((LegendItemClickEvent event) -> {

            ListSeries series = (ListSeries) event.getSeries();

            if (series.isVisible()) {
                optionGroup.unselect(series);
            } else {
                optionGroup.select(series);
            }
        });

        optionGroup = new OptionGroup();
        optionGroup.setId("vaadin-optiongroup");
        optionGroup.setImmediate(true);
        optionGroup.setMultiSelect(true);

        final List<Series> series = chart.getConfiguration().getSeries();
        for (Series series2 : series) {
            optionGroup.addItem(series2);
            optionGroup.setItemCaption(series2, series2.getName());
        }
        optionGroup.setValue(optionGroup.getItemIds());
        demoCharts.addComponentAsFirst(optionGroup);
        optionGroup.addValueChangeListener((Property.ValueChangeEvent event) -> {
            @SuppressWarnings("unchecked")
            Collection<ListSeries> value = (Collection<ListSeries>) event
                    .getProperty().getValue();
            for (Series s : series) {
                ((ListSeries) s).setVisible(value.contains(s));
            }
        });

        chart.drawChart(conf);
        demoCharts.addComponent(chart);

        setContent(demoCharts);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
