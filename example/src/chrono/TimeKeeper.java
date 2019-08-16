package chrono;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: TimeKeeper
// Date: 31/5/14
// Description: A singleton class representing a TimeKeeper.

public class TimeKeeper {
    // Reference to the shared instance of the TimeKeeper
    private static TimeKeeper sharedTimeKeeper;
    
    // Preconditions: None (The getSharedTimeKeeper is created on the first call)
    // Postcondition: A reference to the shared TimeKeeper is returned
    public static TimeKeeper getSharedTimeKeeper() {
        if (sharedTimeKeeper == null)        // Create the TimeKeeper if neccessary
            sharedTimeKeeper = new TimeKeeper();
        
        return sharedTimeKeeper;
    }
    
    // Preconditions: None
    // Postcondition: Returns the current chrono of the shared getSharedTimeKeeper
    public static double currentTime() {
        return getSharedTimeKeeper().getCurrentTime();
    }
    
    // Preconditions: None
    // Postcondition: Sets the chrono of the shared getSharedTimeKeeper
    public static void setTime(double target) {
        getSharedTimeKeeper().jumpTo(target);
    }
    
    // Time of a getSharedTimeKeeper instance
    private double currentTime;
    
    private TimeKeeper() {
        this.currentTime = 0;
    }
    
    // Preconditions: None
    // Postcondition: The recorded chrono of the getSharedTimeKeeper object is returned
    private double getCurrentTime() {
        return this.currentTime;
    }
    
    // Preconditions: None
    // Postcondition: The chrono on the getSharedTimeKeeper is set to the parameter value
    private void jumpTo(double target) {
        this.currentTime = target;
    }
}