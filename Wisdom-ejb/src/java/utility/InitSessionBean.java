package utility;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup
public class InitSessionBean {
    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal; 

    @PostConstruct
    public void init() {
        ejbTimerSessionLocal.createTimers();
    }
}
