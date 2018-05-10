package com.example.juliana.appfeelings.Clases;

public class Test {
    private int idTest;
    private String testName;
    private String link;

    public Test(int idTest, String testName, String link){
        this.idTest = idTest;
        this.testName = testName;
        this.link = link;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
