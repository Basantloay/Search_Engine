import {useState,useEffect} from "react";
import ReactPaginate from 'react-paginate';
import React from "react";
import "./search.css";
import axios from 'axios';
const URL = "/search";
export default function SearchResults(props){   //dah el ha5od fe el result props.title props.url props.description
    const chunkSize = 10;
    const [clients,setClients] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);

    const changePage = ({ selected }) => {
        setCurrentPage(selected);
    };
    const [totNumberofPages,settotNumberofPages] = useState(0);
    const [currentlyDisplayed,setcurrentlyDisplayed] = useState(0);
    var items;
fetch('/search')
.then(function(response){ return response.json(); })
.then(function(data) {
    items = data;
   console.log(items);
    ////////////////////////
        settotNumberofPages(Math.ceil(items.length / chunkSize));
       // console.log({totNumberofPages});
        var displayedPages=chunkSize*currentPage;
        setcurrentlyDisplayed(items.slice(displayedPages, displayedPages + chunkSize)
    .map((DisplayedLinks) => {
        return (
         
            <div className="LinkBlock">
                <a  href= {DisplayedLinks[0]}>
                <h2>{DisplayedLinks[1]}</h2>
                <h5>{DisplayedLinks[0]}</h5>
                <p className="description">{DisplayedLinks[2]}</p>
                </a>
            </div>
         
        );
        })
)})
useEffect(() => {
    fetch('/search')

}, [ items])
    return(
    
        < div className="App">
        {currentlyDisplayed}
        <ReactPaginate
            previousLabel={"Previous"}
            nextLabel={"Next"}
            pageCount={totNumberofPages}
            onPageChange={changePage}
            containerClassName={"paginationBttns"}
            previousLinkClassName={"previousBttn"}
            nextLinkClassName={"nextBttn"}
            disabledClassName={"paginationDisabled"}
            activeClassName={"paginationActive"}
        />
        </div>
    )
}