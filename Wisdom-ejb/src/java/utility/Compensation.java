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

    public Compensation(String month, Double questionIncome) {
        this.month = month;
        this.questionIncome = questionIncome;
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
