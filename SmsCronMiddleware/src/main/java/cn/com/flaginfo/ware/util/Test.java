package cn.com.flaginfo.ware.util;

public class Test {
    private static String id;
    private static String name;
    private static String school;
    static{
        id = "123";
        name = "leit";
        school = "清华大学";
    }
    public static String getId() {
        return id;
    }
    public static void setId(String id) {
        Test.id = id;
    }
    public static String getName() {
        return name;
    }
    public static void setName(String name) {
        Test.name = name;
    }
    public static String getSchool() {
        return school;
    }
    public static void setSchool(String school) {
        Test.school = school;
    }

}
