import Search from './component/search'
import SearchResults from './component/SearchResults'
import {BrowserRouter as Router, Switch , Route} from "react-router-dom";
function App() {
  return (
    <Router>
    <div >
      <Switch>
      <Route path="/" exact component={Search}/>
      
      <Route path="/SearchResults" component={SearchResults}/>
      </Switch>
      </div>
      </Router>
  );
}

export default App;
