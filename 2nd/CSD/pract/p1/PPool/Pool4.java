// CSD feb 2015 Juansa Sendra
public class Pool4 extends Pool {  //kids cannot enter if there are instructors waiting to exit

    private int instructors = 0;
    private int kids = 0;
    private int kiCounter = 0; 
    private int ki = 0;
    private int capCounter = 0;
    private int cap = 0;

    private int instructorsWaitingToExit = 0;

    public void init(int ki, int cap) {
        this.ki = ki;
        this.cap = cap;
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (
            noInstructorsSwimming() || 
            limitCapacityReached() || 
            instructorsWaiting() ||
            limitKiReached()
        ) {
            log.waitingToSwim();
            wait();
        }

        log.swimming();

        kids++; capCounter++;
        recalculateKiCounter();

        notifyAll();
    }

    public synchronized void kidRests() throws InterruptedException {
        log.resting(); 

        kids--; capCounter--;
        recalculateKiCounter();

        notifyAll();
    }

    public synchronized void instructorSwims() throws InterruptedException {
        while(limitCapacityReached()) {
            log.waitingToSwim();
            wait();
        }

        log.swimming();

        instructors++; capCounter++;
        recalculateKiCounter();

        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while((kidsStillSwimming() && onlyOneInstructorSwimming()) || limitKiReached()) {
            log.waitingToRest();
            instructorsWaitingToExit++;
            wait();
        }

        log.resting(); 

        instructors--; capCounter--;
        recalculateKiCounter();
        instructorsWaitingToExit--;
        
        notifyAll();
    }

    private boolean noInstructorsSwimming () {
        return instructors == 0;
    }

    private boolean kidsStillSwimming () {
        return kids > 0;
    }

    private boolean onlyOneInstructorSwimming () {
        return instructors == 1;
    }

    private boolean instructorsWaiting () {
        return instructorsWaitingToExit > 0;
    }

    private boolean limitCapacityReached () {
        return capCounter >= cap;
    }

    private boolean limitKiReached () {
        return kiCounter >= ki;
    }

    private void recalculateKiCounter () {
        if (instructors == 0) { // guard from / 0 division
            kiCounter = 0;
            return;
        }
        
        // force conversion to double
        double kiCounterDouble = ((double) kids / (double)instructors);

        // round the division to the upper, as we need at least the minimum to accomplish ki ratio
        kiCounter = (int) Math.ceil(kiCounterDouble);
    }
}


