/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.AuthorEntity;
import entity.FollowerAnalyticsEntity;
import exception.NoSuchEntityException;
import java.time.LocalDateTime;
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
import sessionBean.BISessionBeanLocal;
import sessionBean.ReaderSessionBeanLocal;

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
    
    @EJB(name = "BISessionBeanLocal")
    private BISessionBeanLocal bISessionBeanLocal;
    
    @EJB(name = "ReaderSessionBeanLocal")
    private ReaderSessionBeanLocal readerSessionBeanLocal;
    
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
        
        LocalDateTime currentTime = LocalDateTime.now();
        bISessionBeanLocal.updateFollowersByMonth(currentTime.getMonthValue(), followerAnalytics.getId(), author.getId());
        
        LineChartModel model = new LineChartModel();
        
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Number of followers in " + followerAnalytics.getCurrentYear());
        
        series1.set("Jan", followerAnalytics.getJan());
        series1.set("Feb", followerAnalytics.getFeb());
        series1.set("Mar", followerAnalytics.getMar());
        series1.set("Apr", followerAnalytics.getApr());
        series1.set("May", followerAnalytics.getMay());
        series1.set("Jun", followerAnalytics.getJun());
        series1.set("Jul", followerAnalytics.getJul());
        series1.set("Aug", followerAnalytics.getAug());
        series1.set("Sep", followerAnalytics.getSep());
        series1.set("Oct", followerAnalytics.getOct());
        series1.set("Nov", followerAnalytics.getNov());
        series1.set("Dec", followerAnalytics.getDecember());
        
        model.addSeries(series1);
        
        return model;
    }
    
    public void testFollow() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());
        
        try {
            readerSessionBeanLocal.followAuthor(authorId, Long.valueOf(1));
        } catch (NoSuchEntityException e) {
            // TODO:
        }
        
    }
}
