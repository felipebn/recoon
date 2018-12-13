import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";
import JobOpenings from './jobOpening/JobOpenings'
import SingleJobOpening from './jobOpening/SingleJobOpening'

//TODO routing
class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route exact path='/' component={JobOpenings}/>
          <Route path='/jobopening/:id' component={SingleJobOpening}/>
        </Switch>
      </Router>
    );
  }
}

export default App;
