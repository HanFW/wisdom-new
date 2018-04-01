/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import entity.FollowerAnalyticsEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import sessionBean.AuthorSessionBeanLocal;

/**
 *
 * @author Yongxue
 */
@Named(value = "authorViewFollowersAnalyticsManagedBean")
@RequestScoped
public class AuthorViewFollowersAnalyticsManagedBean {

    /**
     * Creates a new instance of AuthorViewFollowersAnalyticsManagedBean
     */
    @EJB(name = "AuthorSessionBeanLocal")
    private AuthorSessionBeanLocal authorSessionBeanLocal;

    private LineChartModel followersAnalytics;
    private Long authorId;
    private FollowerAnalyticsEntity followerAnalytics;
    private AuthorEntity author;

    private ExternalContext ec;

    public AuthorViewFollowersAnalyticsManagedBean() {
    }

    @PostConstruct
    public void init() {
        createLineModels();
    }

    public LineChartModel getFollowersAnalytics() {
        return followersAnalytics;
    }

    private void createLineModels() {
        followersAnalytics = initLinearModel();
        followersAnalytics.setTitle("Number of followers");
        followersAnalytics.setLegendPosition("e");
        followersAnalytics.setShowPointLabels(true);
        followersAnalytics.getAxes().put(AxisType.X, new CategoryAxis("Months"));
        Axis yAxis = followersAnalytics.getAxis(AxisType.Y);
        yAxis.setMin(0);
    }

    private LineChartModel initLinearModel() {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());
        author = authorSessionBeanLocal.retrieveAuthorById(authorId);
        followerAnalytics = author.getFollowerAnalytics();

        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Number of followers in " + followerAnalytics.getCurrentYear());

        series1.set("Jan", 120);
        series1.set("Feb", 120);
        series1.set("Mar", 120);
        series1.set("Apr", 120);
        series1.set("May", 120);
        series1.set("Jun", 120);
        series1.set("Jul", 120);
        series1.set("Aug", 120);
        series1.set("Sep", 120);
        series1.set("Oct", 120);
        series1.set("Nov", 120);
        series1.set("Dec", 120);

        model.addSeries(series1);

        return model;
    }
}
