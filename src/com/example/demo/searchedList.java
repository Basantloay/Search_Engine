
package com.example.demo;

import com.company.QueryProcessor.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@RestController
public class searchedList {
    boolean check = true;
    Query q = new Query();
    Vector<Vector<String>> searchList = new Vector<Vector<String>>();
    Vector<String> savedwords = new Vector<String>();
    @RequestMapping("/search")
    public Vector<Vector<String>> search() throws IOException {
            if(q.getSearched() != null && check)
            {
                searchList = q.proccessorQuery();
                check=false;
            }
        return searchList;
      //  return List.of("Hello","World");
    }
    @PostMapping("/search")
    public void getSearchedWord( @RequestBody String word)
    {
        check=true;
        q.SetSearched(word.replace("=",""));
        if(!savedwords.contains(word.replace("=","")))
        {
            savedwords.add(word.replace("=", ""));
        }

        //System.out.println(savedwords);

    }
    @RequestMapping("/alreadysearchedwords")
    public    Vector<String> alreadySearchedWords()
    {
        return savedwords;
    }
}
