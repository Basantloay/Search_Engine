package com.company.Indexer;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Word {
    String value;
    int DF;
    Vector<List<String>> ListOfDocuments;   //List[0] = {id_of_Document, TF, places[TF]}
    Word(String Value){
        this.value = Value;
        DF = 0;
        ListOfDocuments = new Vector<List<String>>();
    }

    public int getDF() {
        return DF;
    }

    public String getValue() {
        return value;
    }

    public Vector<List<String>> getListOfDocuments() {
        return ListOfDocuments;
    }

    public void setDF(int DF) {
        this.DF = DF;
    }

    public void setListOfDocuments(Vector<List<String>> listOfDocuments) {
        ListOfDocuments = listOfDocuments;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void insertList(List<String> list)
    {
        ListOfDocuments.add(list);
    }
    public void Print()
    {
        System.out.print(this.value + " ");
        System.out.println(this.DF);
        for(int i = 0; i < ListOfDocuments.size(); i++)
        {
            for(int j = 0; j < ListOfDocuments.get(i).size(); j++)
            {
                System.out.print(ListOfDocuments.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
    public void UpdateDF()
    {
        this.DF = ListOfDocuments.size();
    }

}
