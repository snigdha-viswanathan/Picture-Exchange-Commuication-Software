package ssn.codebreakers.pecsinstructor.helpers;

import java.util.UUID;

public class CommonUtils
{
    public static String getUniqueRandomID()
    {
        return UUID.randomUUID().toString();
    }
}
