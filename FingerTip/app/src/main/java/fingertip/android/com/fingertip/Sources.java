package fingertip.android.com.fingertip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NILESH on 05-03-2017.
 */

public class Sources {

    public String username;
    public List<String> sourceList;

    public Sources() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        username = "Guest";
        sourceList = new ArrayList<String>();
    }

    public Sources(String username, List<String> sourceList) {
        this.username = username;
        this.sourceList = sourceList;
    }

    public String getUserName()
    {
        return username;
    }

    public List<String> getSourcesList()
    {
        return sourceList;
    }
}
