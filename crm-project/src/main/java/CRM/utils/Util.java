package CRM.utils;

import CRM.entity.Attribute;
import CRM.entity.SharedContent;

import java.util.Arrays;
import java.util.List;

public class Util {
    public static String sectionId = "section.id";
    public static List<Class> fatherClasses(){
        return Arrays.asList(SharedContent.class, Attribute.class);
    }

}
