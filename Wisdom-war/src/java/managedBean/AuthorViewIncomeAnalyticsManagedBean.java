/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import entity.ArticleEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.PieChartModel;
import sessionBean.ArticleSessionBeanLocal;
import utility.Constants;

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
    @EJB(name = "ArticleSessionBeanLocal")
    private ArticleSessionBeanLocal articleSessionBeanLocal;

    private PieChartModel averageIncome;
    private Long authorId;
    private String articleTopic;
    private HashMap<String, Double> totalIncomePerTopic = new HashMap<String, Double>();
    private Double artsIncome = 0.0;
    private Double fashionIncome = 0.0;
    private Double foodIncome = 0.0;
    private Double lifestyleIncome = 0.0;
    private Double musicIncome = 0.0;
    private Double photographyIncome = 0.0;
    private Double politicsIncome = 0.0;
    private Double sportsIncome = 0.0;
    private Double technologyIncome = 0.0;
    private Double travelIncome = 0.0;

    private String key;
    private Double value;
    private Integer size;

    private ExternalContext ec;

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

        ec = FacesContext.getCurrentInstance().getExternalContext();

        authorId = Long.valueOf(ec.getSessionMap().get("authorId").toString());
        List<ArticleEntity> article = articleSessionBeanLocal.getArticlesByAuthorId(authorId);

        for (int i = 0; i < article.size(); i++) {
            articleTopic = article.get(i).getTopic();

            switch (articleTopic) {
                case Constants.TOPIC_ARTS:
                    artsIncome = artsIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_FASHION:
                    fashionIncome = fashionIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_FOOD:
                    foodIncome = foodIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_LIFESTYLE:
                    lifestyleIncome = lifestyleIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_MUSIC:
                    musicIncome = musicIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_PHOTOGRAPHY:
                    photographyIncome = photographyIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_POLITICS:
                    politicsIncome = politicsIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_SPORTS:
                    sportsIncome = sportsIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_TECHNOLOGY:
                    technologyIncome = technologyIncome + article.get(i).getTotalIncome();
                    break;
                case Constants.TOPIC_TRAVEL:
                    travelIncome = travelIncome + article.get(i).getTotalIncome();
                    break;
            }
        }

        if (artsIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_ARTS, artsIncome);
        }
        if (fashionIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_FASHION, fashionIncome);
        }
        if (foodIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_FOOD, foodIncome);
        }
        if (lifestyleIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_LIFESTYLE, lifestyleIncome);
        }
        if (musicIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_MUSIC, musicIncome);
        }
        if (photographyIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_PHOTOGRAPHY, photographyIncome);
        }
        if (politicsIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_POLITICS, politicsIncome);
        }
        if (sportsIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_SPORTS, sportsIncome);
        }
        if (technologyIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_TECHNOLOGY, technologyIncome);
        }
        if (travelIncome != 0.0) {
            totalIncomePerTopic.put(Constants.TOPIC_TRAVEL, travelIncome);
        }

        averageIncome = new PieChartModel();

        if (totalIncomePerTopic.isEmpty()) {
            averageIncome.set("No Article Found", 0);
        } else {
            for (Map.Entry totalIncomeMap : totalIncomePerTopic.entrySet()) {
                key = totalIncomeMap.getKey().toString();
                value = Double.valueOf(totalIncomeMap.getValue().toString());
                size = articleSessionBeanLocal.getArticlesByTopic(key).size();

                System.out.println("size " + size);
                averageIncome.set(key, (value / size));
            }
        }

        averageIncome.setTitle("Average Income per Article Topic");
        averageIncome.setLegendPosition("w");
    }

    public void testTipIncome1() {

        ReaderEntity reader = new ReaderEntity();

        try {
            reader = articleSessionBeanLocal.tipArticle(Long.valueOf(1), Long.valueOf(1), 13.0);
        } catch (InsufficientBalanceException e) {

        }
    }

    public void testTipIncome2() {

        ReaderEntity reader = new ReaderEntity();

        try {
            reader = articleSessionBeanLocal.tipArticle(Long.valueOf(1), Long.valueOf(1), 8.0);
        } catch (InsufficientBalanceException e) {

        }
    }

    public void testTipIncome3() {

        ReaderEntity reader = new ReaderEntity();

        try {
            reader = articleSessionBeanLocal.tipArticle(Long.valueOf(1), Long.valueOf(2), 15.0);
        } catch (InsufficientBalanceException e) {

        }
    }

    public void testTipIncome4() {

        ReaderEntity reader = new ReaderEntity();

        try {
            reader = articleSessionBeanLocal.tipArticle(Long.valueOf(1), Long.valueOf(2), 24.0);
        } catch (InsufficientBalanceException e) {

        }
    }
}
