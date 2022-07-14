import './App.less';
import React from "react";
import { BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import { createBrowserHistory } from "history";
import Start from "./components/Start";
import Quiz from "./components/Quiz";

const history = createBrowserHistory();


function App() {
  return (
    <div className="App">
      <Router history={history}>
        <Switch>
          <Route path={"/"} component={Start} exact />
          <Route path={"/quiz"} component={Quiz} />
        </Switch>
      </Router>
    </div>
  );
}

export default App;
