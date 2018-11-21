import React from 'react';
import ReactDOM from 'react-dom';
import ApolloClient from "apollo-boost";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
import { ApolloProvider } from 'react-apollo';

import * as serviceWorker from './serviceWorker';
import './styles/css/index.css';
import App from './App';

const apollo = new ApolloClient({
    uri:"http://localhost:8080/graphql"
});

ReactDOM.render(
    <ApolloProvider client={apollo}>
      <App />
    </ApolloProvider>,
    document.getElementById('root'),
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
