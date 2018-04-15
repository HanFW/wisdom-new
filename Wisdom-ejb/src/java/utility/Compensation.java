/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Yongxue
 */
public class Compensation {

    private String month;
    private Double questionIncome;
    private Integer year;

    public Compensation(Integer year, String month, Double questionIncome) {
        this.year = year;
        this.month = month;
        this.questionIncome = questionIncome;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getQuestionIncome() {
        return questionIncome;
    }

    public void setQuestionIncome(Double questionIncome) {
        this.questionIncome = questionIncome;
    }
}
