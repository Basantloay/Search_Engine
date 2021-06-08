import React from 'react'
import { useState, useEffect } from "react";
import './search.css'
import './custom'
import { ShowSuggestions } from './actions'
import axios from 'axios';
import SearchResults from './SearchResults';
import { Link, Route, useParams } from 'react-router-dom'

export default function Search()
 {
    const [previousSearch,setpreviousSearch]=useState([]);
    const [matcheswords,setmatchesSearch]=useState([]);
    fetch('/alreadysearchedwords')
    .then(function(response){ return response.json(); })
    .then(function(words) {
        setpreviousSearch(words);
        //console.log(words);

    });
    const [SearchText, setSearchText] = useState("");
    //const showDropDown = e => setSearchText(e.target.value);
    function showDropDown(e) 
    {    
        let matchesSearch=[]
        setSearchText(e.target.value);
        console.log({SearchText});
        if(SearchText.length > 0 )
        {
            
            matchesSearch= previousSearch.filter(res => {
                const regx = new RegExp(`${SearchText}`,"gi");
                return res.match(regx);
            })
        }
        console.log("Matched");
        console.log(matchesSearch);
        setmatchesSearch(matchesSearch);
    }
    const navStyle = {
        color: 'black'
    };
    function searchword(e) {

        axios.post("/search", SearchText).then(response => {
           console.log(response);
            return (
                //  <div>
                <SearchResults />
                // </div>
            );
        })
    };
  function suggestedword(str)
  {
    axios.post("/search", str).then(response => {
        console.log(response);
         return (
             //  <div>
             <SearchResults />
             // </div>
         );
     })

  }
    return (
        <div className="totSearch">
            <div className="overlay"></div>
            <h1>Search</h1>
            <div className="searchAndResults">
                <div className="input-group mb-3">
                    <input type="text" onChange={showDropDown} autoComplete ="off" className="form-control" id="myinput" placeholder="Hello, How Can We Help You ?" aria-label="Recipient's username" aria-describedby="button-addon2"/>
                    <div className="input-group-append">
                    <Link  style={navStyle} to={`/SearchResults`}>
                        <button className="btn btn-outline-secondary" type="button" id="button-addon2" onClick={searchword}>search</button>
                   </Link>
                    </div>
                </div>
             {SearchText &&    <ul className="list-group">
                    {matcheswords.map(pSearch=> (     <Link  style={navStyle} to={`/SearchResults`}><li onClick={suggestedword(pSearch)} className="list-group-item">{pSearch}</li> </Link> ) )}
                    {/* <li className="list-group-item active" aria-current="true">An active item</li>
                    <li className="list-group-item">A second item</li>
                    <li className="list-group-item">A third item</li>
                    <li className="list-group-item">A fourth item</li>
                    <li className="list-group-item">And a fifth one</li> */}
                </ul>}
            </div>
        </div>
    )
}