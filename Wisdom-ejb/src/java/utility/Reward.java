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
public class Reward {

    private String month;
    private Double rewardIncome;
    private Integer year;

    public Reward(Integer year, String month, Double rewardIncome) {
        this.year = year;
        this.month = month;
        this.rewardIncome = rewardIncome;
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

    public Double getRewardIncome() {
        return rewardIncome;
    }

    public void setRewardIncome(Double rewardIncome) {
        this.rewardIncome = rewardIncome;
    }
}
