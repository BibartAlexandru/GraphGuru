package com.example.graphguru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1 ;
    private static final String DB_NAME = "GraphGuru" ;

    //region fields for users table
    private static  final String user_table_name = "users" ;
    private  static  final  String user_id_col = "id" ;
    private  static  final  String user_name_col = "name" ;
    private  static  final  String user_password_col = "password" ;
    private  static  final  String user_graph_collection_table_name_col = "graph_collection" ;
    //endregion

    //region fields for graph collection table
    private static  final String graph_collection_graph_table_name = "graph_table_name" ;
    private static  final String graph_collection_id = "id" ;
    //endregion

    //region fields in graph table
    private static final String graph_table_id_col = "id" ;
    private static final String graph_table_from_node_col = "from_node" ;
    private static final String graph_table_to_node_col = "to_node" ;
    private static final String graph_table_weight_col = "weight" ;
    //endregion
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + user_table_name + " ("
                + user_id_col + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + user_name_col + " TEXT, "
                + user_password_col + " TEXT, "
                + user_graph_collection_table_name_col + " TEXT)" ;
        db.execSQL(query);
    }
    public boolean addUser(String name,String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(user_name_col, name);
        values.put(user_password_col, password);
        values.put(user_graph_collection_table_name_col, name + "_graph_collection");

        long result = db.insert(user_table_name, null, values);

        if(result == -1)
            return false ;
        try {
            String query = "CREATE TABLE " + name + "_graph_collection ("
                    + graph_collection_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + graph_collection_graph_table_name + " TEXT);";
            db.execSQL(query);
            return true ;
        }
        catch (Exception e)
        {
            return false ;
        }
        finally {
            db.close();
        }

    }

    public void ReplaceGraph(Graph graph, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + graph.name ;
        if(db != null)
            db.execSQL(query);
        for (int i = 0; i < graph.nodes.size(); i++)
            for (int j = 0; j < graph.adj.get(i).size(); j++) {
                Log.d("GRAPH_CHECK",String.valueOf(i) + " " + String.valueOf(j) + " " + graph.edgeWeight.get(i).get(j)) ;
                ContentValues edgeValues = new ContentValues();
                edgeValues.put(graph_table_from_node_col, i);
                edgeValues.put(graph_table_to_node_col, graph.GetNodeIndex(graph.adj.get(i).get(j)));
                edgeValues.put(graph_table_weight_col, graph.edgeWeight.get(i).get(j));
                long succes = db.insert(graph.name, null, edgeValues);
                Log.d("AJUTOR", "DB INSERT ESTE : " + String.valueOf(succes));
            }
        for(int i = 0 ; i < graph.nodes.size() ; i++)
        {
            ContentValues edgeValues = new ContentValues();
            edgeValues.put(graph_table_from_node_col, i);
            edgeValues.put(graph_table_to_node_col, i);
            edgeValues.put(graph_table_weight_col, 0);
            db.insert(graph.name, null, edgeValues);
        }
        db.close();
    }
    public boolean CreateGraph(Graph graph, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String statement = "CREATE TABLE " + graph.name + " ("
                    + graph_table_id_col + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + graph_table_from_node_col + " INTEGER, "
                    + graph_table_to_node_col + " INTEGER, "
                    + graph_table_weight_col + " REAL)"; //e float
            db.execSQL(statement);
            for (int i = 0; i < graph.nodes.size(); i++)
                for (int j = 0; j < graph.adj.get(i).size(); j++) {
                    ContentValues edgeValues = new ContentValues();
                    edgeValues.put(graph_table_from_node_col, i);
                    edgeValues.put(graph_table_to_node_col, graph.GetNodeIndex(graph.adj.get(i).get(j)));
                    edgeValues.put(graph_table_weight_col, graph.edgeWeight.get(i).get(j));
                    long succes = db.insert(graph.name, null, edgeValues);
                    Log.d("AJUTOR", "DB INSERT ESTE : " + String.valueOf(succes));
                }
            for(int i = 0 ; i < graph.nodes.size() ; i++)
            {
                ContentValues edgeValues = new ContentValues();
                edgeValues.put(graph_table_from_node_col, i);
                edgeValues.put(graph_table_to_node_col, i);
                edgeValues.put(graph_table_weight_col, 0);
                db.insert(graph.name, null, edgeValues);
            }
            ContentValues newGraphTable = new ContentValues();
            newGraphTable.put(graph_collection_graph_table_name, graph.name);
            db.insert(username + "_graph_collection", null, newGraphTable);
        }
        catch (Exception e)
        {
            return false ;
        }
        finally {
            db.close();
        }
        return true ;
    }

    public ArrayList<String> FetchUserGraphs(String username)
    {
        ArrayList<String> table_names = new ArrayList<>() ;
        String query = "SELECT " + graph_collection_graph_table_name + " FROM " + username + "_graph_collection" ;
        SQLiteDatabase db = this.getWritableDatabase() ;

        Cursor results = null ;
        if(db != null)
            results = db.rawQuery(query,null) ;
        while (results.moveToNext())
            table_names.add(results.getString(0)) ;
        return table_names ;
    }

    public User FetchUser(String name, String password)
    {
        User foundUser = null ;
        String query = "SELECT * FROM " + user_table_name + " WHERE " + user_name_col + " = '" + name
                + "' AND " + user_password_col + " = '" + password + "'" ;
        SQLiteDatabase db = this.getWritableDatabase() ;

        Cursor results = null ;
        if(db != null){ // ??
            results = db.rawQuery(query,null) ;
        }
        if(results.getCount() == 0)
            return null ;
        else{
            results.moveToNext();
            foundUser = new User(results.getString(1), results.getString(2),results.getString(3)) ;
            return foundUser ;
        }
    }

    public Graph FetchGraph(String graph_name)
    {
        UndirectedGraph graph = new UndirectedGraph() ;
        if(graph_name.equals(""))
        {
            graph.name = "" ;
            return graph ;
        }
        graph.name = graph_name ;
        String query = "SELECT * FROM " + graph_name ;
        SQLiteDatabase db = this.getWritableDatabase() ;
        Cursor results = null ;
        if(db != null)
            results = db.rawQuery(query,null) ;
        ArrayList<Edge> edges = new ArrayList<>() ;
        while (results.moveToNext())
            edges.add(new Edge(results.getInt(1),results.getInt(2),results.getInt(3))) ;
        for(int i = 0 ; i < edges.size() ; i++)
            if(edges.get(i).from == edges.get(i).to)
                graph.AddNode(graph.GetSmallestNonExistingNodeValue());
        for(int i = 0 ; i < edges.size() ; i++)
            if(edges.get(i).from != edges.get(i).to)
                graph.AddEdge(edges.get(i).from,edges.get(i).to,edges.get(i).weight,true);

            Log.d("FETCH_GRAPH",graph.toString()) ;
        return graph ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }
}
