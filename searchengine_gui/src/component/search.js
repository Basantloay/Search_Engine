import React from 'react'
import './search.css'
import './custom'
import {ShowSuggestions} from'./actions'
export default function Search(){
    return(
        <div className="totSearch">
            <div className="searchAndResults">
            <div className="input-group mb-3">
                <input type="text" onKeyPress={ShowSuggestions} className="form-control" id="myinput" placeholder="Hello, How Can We Help You ?" aria-label="Recipient's username" aria-describedby="button-addon2"/>
                <div className="input-group-append">
                        <button className="btn btn-outline-secondary" type="button" id="button-addon2">search</button>
                </div>
            </div>
            <ul className="list-group none">
                <li className="list-group-item active" aria-current="true">An active item</li>
                <li className="list-group-item">A second item</li>
                <li className="list-group-item">A third item</li>
                <li className="list-group-item">A fourth item</li>
                <li className="list-group-item">And a fifth one</li>
            </ul>
            </div>
        </div>
    )
}