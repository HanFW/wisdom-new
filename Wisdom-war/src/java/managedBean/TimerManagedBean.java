/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import utility.EjbTimerSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "timerManagedBean")
@SessionScoped
public class TimerManagedBean implements Serializable{
    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;
    
    private boolean timerStarted;
    private boolean timerCanceled;

    /**
     * Creates a new instance of TimerManagedBean
     */
    public TimerManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        timerStarted = true;
        timerCanceled = false;
    }
    
    public void startTimer(ActionEvent event) {
        ejbTimerSessionBeanLocal.createTimers();
        timerStarted = true;
        timerCanceled = false;
    }
    
    public void cancelTimer(ActionEvent event) {
        ejbTimerSessionBeanLocal.cancelTimers();
        timerStarted = false;
        timerCanceled = true;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(boolean timerStarted) {
        this.timerStarted = timerStarted;
    }

    public boolean isTimerCanceled() {
        return timerCanceled;
    }

    public void setTimerCanceled(boolean timerCanceled) {
        this.timerCanceled = timerCanceled;
    }
}
