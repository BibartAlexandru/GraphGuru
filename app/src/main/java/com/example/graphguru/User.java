package com.example.graphguru;

public class User {
    private String name ;
    private String password ;
    private String graph_collection_table_name ;

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getGraph_collection_table_name(){
        return graph_collection_table_name;
    }

    public User(String name, String password, String graph_collection_table_name)
    {
        this.name = name ;
        this.password = password ;
        this.graph_collection_table_name = graph_collection_table_name ;
    }


}
