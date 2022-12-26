package CRM.utils;

import CRM.entity.Attribute;
import CRM.entity.SharedContent;

import java.util.Arrays;
import java.util.List;

public class Util {
    public static String sectionId = "section.id";
    public static String myBoards = "My_Boards";
    public static String SharedBoards = "Shared_Boards";
    public static List<Class> fatherClasses(){
        return Arrays.asList(SharedContent.class, Attribute.class);
    }

    public static List<String> permissionPathsForAll = List.of("auth", "board");
    public static List<String> permissionPathsForLeaders = List.of("item", "comment");
    public static List<String> permissionPathsForUsers = List.of("item", "comment");



}
