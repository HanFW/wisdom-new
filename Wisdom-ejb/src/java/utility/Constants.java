package utility;

/**
 *
 * @author sherry
 */
public class Constants {

    // entity status
    public static final String STATUS_DELETED = "DELETED"; // for deleted entities 
    public static final String STATUS_ACTIVE = "ACTIVE"; // for active entities 

    // question status
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ANSWERED = "ANSWERED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    // interest topics
    public static final String TOPIC_ARTS = "Arts";
    public static final String TOPIC_FASHION = "Fashion";
    public static final String TOPIC_FOOD = "Food";
    public static final String TOPIC_LIFESTYLE = "Lifestyle";
    public static final String TOPIC_MUSIC = "Music";
    public static final String TOPIC_PHOTOGRAPHY = "Photography";
    public static final String TOPIC_POLITICS = "Politics";
    public static final String TOPIC_SPORTS = "Sports";
    public static final String TOPIC_TECHNOLOGY = "Technology";
    public static final String TOPIC_TRAVEL = "Travel";

    //Response Error Code
    // 1000-1999 COMMON INFRA.
    public static final int ERROR_ENTITY_NOT_FOUND = 1001;
    public static final int ERROR_AUTH_FAIED = 1002;
    public static final int ERROR_ENTITY_CONFLICT = 1003;
    // 2000-2999
    public static final int ERROR_INVALID_INPUT = 2001;
    public static final int ERROR_QUERY_RESULT = 2002;
    // 3000-3999 

    // 4000-4999 FINANCE
    public static final int ERROR_INSUFFICIENT_BALANCE = 4002;
    public static final int ERROR_DOWNSIZE_CONFLICT = 4003;

    //File Path
    public static final String FILE_PATH = "../altdocroot/";
    //File Type

    //host name
    public static final String HOST
            = "https://is41031718it01.southeastasia.cloudapp.azure.com:8080/";
//            = "https://localhost:8181/";

}
