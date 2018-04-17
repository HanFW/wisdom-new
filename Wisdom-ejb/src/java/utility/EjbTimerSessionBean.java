/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import entity.AuthorEntity;
import entity.QuestionEntity;
import exception.NoSuchEntityException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sessionBean.AuthorSessionBeanLocal;
import sessionBean.QuestionSessionBeanLocal;
import sessionBean.ReaderSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {
    @EJB
    private ReaderSessionBeanLocal readerSessionBeanLocal;
    @EJB
    private AuthorSessionBeanLocal authorSessionBeanLocal;
    @EJB
    private QuestionSessionBeanLocal questionSessionBeanLocal;

    private final String TIMER_EXPIRY = "EJB_TIMER_10000MS";
    private final int EXPIRY_DURATION = 10000;
    
    @Resource
    private SessionContext ctx;
    @PersistenceContext
    private EntityManager em;
    
    public EjbTimerSessionBean()
    {
    }
    
    @Override
    public void createTimers()
    {
        System.out.println("*** Expiry Timer started");
        TimerService timerService = ctx.getTimerService();
        Timer expiryTimer = timerService.createTimer(EXPIRY_DURATION, EXPIRY_DURATION, new String(TIMER_EXPIRY));
    }
    
    @Override
    public void cancelTimers() {
        TimerService timerService = ctx.getTimerService();
        Collection timers = timerService.getTimers();

        for (Object obj : timers) {
            javax.ejb.Timer timer = (javax.ejb.Timer) obj;

            if (timer.getInfo().toString().equals(TIMER_EXPIRY))
            {
                timer.cancel();
                System.out.println("*** Expiry Timer cancelled");
            }
        }
    }
    
    @Timeout
    public void handleTimeout(javax.ejb.Timer timer) {
        if (timer.getInfo().toString().equals(TIMER_EXPIRY)) {
            handleTimeout_Expiry();
//            handleTimeout_Question();
        } else {
            System.out.println("*** Unknown timer timeout: " + timer.getInfo().toString());
        }
    }
    
    private void handleTimeout_Expiry() {
        System.out.println("*** Expiry timer timeout on " + LocalDateTime.now());
        questionSessionBeanLocal.checkExpiredQuestions();
    }
    
//    private void handleTimeout_Question() {
//        QuestionEntity question = new QuestionEntity("Title New", "Content new content new content new");
//        AuthorEntity author = authorSessionBeanLocal.retrieveAuthorById(Long.parseLong("1"));
//        question.setAuthor(author);
//        try {
//            question.setReader(readerSessionBeanLocal.authenticateReader("reader@gmail.com", "password"));
//            question.setPrice(author.getQtnPrice());
//        } catch (NoSuchEntityException ex) {
//            Logger.getLogger(InitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        em.persist(question);
//        em.flush();
//        em.refresh(question);
//        System.out.println("New question " + question.getId() + " created on " + question.getCreated());
//    }
}
