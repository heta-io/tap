/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Current Graphiql version: 0.11.5 */



/**
 * This GraphiQL example illustrates how to use some of GraphiQL's props
 * in order to enable reading and updating the URL parameters, making
 * link sharing of queries a little bit easier.
 *
 * This is only one example of this kind of feature, GraphiQL exposes
 * various React params to enable interesting integrations.
 */
  // Parse the search string to get url parameters.
// var search = window.location.search;
// var parameters = {};
// parameters.variables = exampleVariables;
// parameters.query = exampleQueries;
//
// search.substr(1).split('&').forEach(function (entry) {
//   var eq = entry.indexOf('=');
//   if (eq >= 0) {
//     parameters[decodeURIComponent(entry.slice(0, eq))] =
//       decodeURIComponent(entry.slice(eq + 1));
//   }
// });
// if variables was provided, try to format it.
// if (parameters.variables) {
//   try {
//     parameters.variables =
//       JSON.stringify(JSON.parse(parameters.variables), null, 2);
//   } catch (e) {
//     // Do nothing, we want to display the invalid JSON as a string, rather
//     // than present an error.
//   }
// }
// When the query and variables string is edited, update the URL bar so
// that it can be easily shared
// function onEditQuery(newQuery) {
//   parameters.query = newQuery;
//   updateURL();
// }
// function onEditVariables(newVariables) {
//   parameters.variables = newVariables;
//   updateURL();
// }
// function onEditOperationName(newOperationName) {
//   parameters.operationName = newOperationName;
//   updateURL();
// }
// function updateURL() {
//   var newSearch = '?' + Object.keys(parameters).filter(function (key) {
//     return Boolean(parameters[key]);
//   }).map(function (key) {
//     return encodeURIComponent(key) + '=' +
//       encodeURIComponent(parameters[key]);
//   }).join('&');
//   history.replaceState(null, null, newSearch);
// }
// Defines a GraphQL fetcher using the fetch API. You're not required to
// use fetch, and could instead implement graphQLFetcher however you like,
// as long as it returns a Promise or Observable.
// function graphQLFetcher(graphQLParams) {
//   // This example expects a GraphQL server at the path /graphql.
//   // Change this to point wherever you host your GraphQL server.
//   return fetch('/graphql', {
//     method: 'post',
//     headers: {
//       'Accept': 'application/json',
//       'Content-Type': 'application/json',
//     },
//     body: JSON.stringify(graphQLParams),
//     credentials: 'include',
//   }).then(function (response) {
//     return response.text();
//   }).then(function (responseBody) {
//     try {
//       return JSON.parse(responseBody);
//     } catch (error) {
//       return responseBody;
//     }
//   });
// }
// Render <GraphiQL /> into the body.
// See the README in the top level of this module to learn more about
// how you can customize GraphiQL by providing different values or
// additional child elements.
// ReactDOM.render(
//   React.createElement(GraphiQL, {
//     fetcher: graphQLFetcher
//     //query: parameters.query,
//     //variables: parameters.variables,
//     //operationName: parameters.operationName,
//     //onEditQuery: onEditQuery,
//     //onEditVariables: onEditVariables,
//     //onEditOperationName: onEditOperationName
//   }),
//   document.getElementById('graphiql')
// );

// import React from 'react';
// import ReactDOM from 'react-dom';
// import GraphiQL from 'graphiql';
// import fetch from 'isomorphic-fetch';

//import GraphQLQueries;

function graphQLFetcher(graphQLParams) {
    return fetch(window.location.origin + '/graphql', {
        method: 'post',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(graphQLParams),
    }).then(response => response.json());
}

//ReactDOM.render(<GraphiQL fetcher={graphQLFetcher} />, document.body);
ReactDOM.render(
    React.createElement(GraphiQL, {
        fetcher: graphQLFetcher //,
        //query: GraphQLQueries.exampleQueries
    }),
    document.getElementById('graphiql')
);