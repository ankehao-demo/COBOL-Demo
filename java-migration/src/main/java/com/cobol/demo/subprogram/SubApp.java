package com.cobol.demo.subprogram;

public class SubApp {
    private String wsTestItem1 = "";
    private String wsTestItem2 = "";

    public void callByContent(String item1, String item2) {
        String lsTestItem1 = "";
        String lsTestItem2 = "";
        
        System.out.println("In sub program: " + item1 + " " + item2);
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Moving linkage section values to ws and ls vars..");
        
        wsTestItem1 = item1;
        wsTestItem2 = item2;
        lsTestItem1 = item1;
        lsTestItem2 = item2;
        
        System.out.println("setting input variables to new value...");
        
        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: replace1 replace2");
    }

    public void callByReference(StringBuilder item1, StringBuilder item2) {
        String lsTestItem1 = "";
        String lsTestItem2 = "";
        
        System.out.println("In sub program: " + item1 + " " + item2);
        System.out.println();
        System.out.println("working-storage values at start:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at start:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Moving linkage section values to ws and ls vars..");
        
        wsTestItem1 = item1.toString();
        wsTestItem2 = item2.toString();
        lsTestItem1 = item1.toString();
        lsTestItem2 = item2.toString();
        
        System.out.println("setting input variables to new value...");
        item1.setLength(0);
        item1.append("replace1");
        item2.setLength(0);
        item2.append("replace2");
        
        System.out.println();
        System.out.println("working-storage values at end:");
        System.out.println("ws-test-item-1: " + wsTestItem1);
        System.out.println("ws-test-item-2: " + wsTestItem2);
        System.out.println();
        System.out.println("local-storage values at end:");
        System.out.println("ls-test-item-1: " + lsTestItem1);
        System.out.println("ls-test-item-2: " + lsTestItem2);
        System.out.println();
        System.out.println("Exit sub program: " + item1 + " " + item2);
    }

    public void cancel() {
        wsTestItem1 = "";
        wsTestItem2 = "";
    }
}
