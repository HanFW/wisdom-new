/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewIncomeAnalyticsManagedBean")
@RequestScoped
public class AuthorViewIncomeAnalyticsManagedBean {

    /**
     * Creates a new instance of AuthorViewIncomeAnalyticsManagedBean
     */
    private PieChartModel averageIncome;

    public AuthorViewIncomeAnalyticsManagedBean() {
    }

    @PostConstruct
    public void init() {
        createPieModels();
    }

    public PieChartModel getAverageIncome() {
        return averageIncome;
    }

    public void setAverageIncome(PieChartModel averageIncome) {
        this.averageIncome = averageIncome;
    }

    private void createPieModels() {
        createPieModel1();
    }

    private void createPieModel1() {
        averageIncome = new PieChartModel();

        averageIncome.set("Brand 1", 540);
        averageIncome.set("Brand 2", 325);
        averageIncome.set("Brand 3", 702);
        averageIncome.set("Brand 4", 421);

        averageIncome.setTitle("Simple Pie");
        averageIncome.setLegendPosition("w");
    }
}
