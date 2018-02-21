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


var instructions = "# AWAGraphiQL Interface\n" +
    "#\n" +
    "# AWAGraphiQL is an in-browser IDE for writing, validating, and\n" +
    "# testing GraphQL queries.\n" +
    "# Type queries into this side of the screen, and you will\n" +
    "# see intelligent typeaheads aware of the current GraphQL type schema and\n" +
    "# live syntax and validation errors highlighted within the text.\n" +
    "# To bring up the auto-complete at any point, just press Ctrl-Space.\n" +
    "# Press the run button above, or Cmd-Enter to execute the query, and the result\n" +
    "# will appear in the pane to the right.\n\n" +
    "# This interface is specifically for testing TAP queries that are used by AWA.\n" +
    "\n";

var awaQueries = "# AWA Specific Queries\n" +
    "# -------------------\n" +
    "# These queries are just examples of what TAP can do, the\n" +
    "# actual capability of the server at any point in time can\n" +
    "# be found in the Schema - see the Documentation Explorer\n" +
    "# on the right hand side. \n" +
    "# See the TAP Documentation https://heta-io.github.io/tap/\n" +
    "\n" +
    "# All queries require submission of text and return\n" +
    "# analytics,timestamp, message, querytime\n" +
    "\n" +
    "# Some queries like \"moves\" require external resources and may\n" +
    "# fail if those resources are not available.\n" +
    "\n" +
    "query RhetoricalMoves($input: String!) {\n" +
    "  moves(text:$input,grammar:\"analytic\") {\n" +
    "    analytics\n" +
    "    message\n" +
    "    timestamp\n" +
    "    querytime\n" +
    "  }\n" +
    "}\n" +
    "# However, you only need to ask for what you want e.g.\n" +
    "query MinimalMoves($input:String!) {\n" +
    "  moves(text:$input) {\n" +
    "    analytics\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "# Tokenise with pipetype (default is 'fast'):\n" +
    "# fast - lemmas and postags\n" +
    "# standard - lemmas, postags, parse data\n" +
    "# ner - lemmas, postags, parse data, nertags\n" +
    "\n" +
    "query Tokenise($input: String!) {\n" +
    "  annotations(text:$input) {\n" +
    "    analytics {\n" +
    "      idx\n" +
    "      start\n" +
    "      end\n" +
    "      length\n" +
    "      tokens {\n" +
    "        idx\n" +
    "        term\n" +
    "        lemma\n" +
    "        postag\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "query Expressions($input2:String!) {\n" +
    "  expressions(text:$input2) {\n" +
    "    analytics {\n" +
    "      sentIdx\n" +
    "      affect{\n" +
    "        text\n" +
    "      }\n" +
    "      epistemic {\n" +
    "        text\n" +
    "        startIdx\n" +
    "        endIdx\n" +
    "      }\n" +
    "      modal {\n" +
    "        text\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}\n" +
    "query Vocab($input: String!) {\n" +
    "  vocabulary(text:$input){\n" +
    "    analytics {\n" +
    "      unique\n" +
    "      terms {\n" +
    "        term\n" +
    "        count\n" +
    "      }\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "query Metrics($input: String!) {\n" +
    "  metrics(text:$input) {\n" +
    "    analytics {\n" +
    "      sentences\n" +
    "      tokens\n" +
    "      words\n" +
    "      characters\n" +
    "      punctuation\n" +
    "      whitespace\n" +
    "      sentWordCounts\n" +
    "      averageSentWordCount\n" +
    "      wordLengths\n" +
    "      averageWordLength\n" +
    "      averageSentWordLength\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n"

var awaVariables = "{\"input\": \"It didn't take any time for Dr. Smith to review the subject outline by logging onto UTS Online. However, I walked into class like a blank canvas. I had no idea what this course was about but I was certain it had something to do with responsibility and leaders. I reflected on this and felt decision making was like second nature, yes I over-thought my decisions whether it was personal or professional but I never thought of the act of having to justify my decisions.\"," +
    "\"input2\": \"Althogh I wasn't certain, I did believe that I was doing the right thing. Next time I will be sure.\"}"



/**
 * This GraphiQL example illustrates how to use some of GraphiQL's props
 * in order to enable reading and updating the URL parameters, making
 * link sharing of queries a little bit easier.
 *
 * This is only one example of this kind of feature, GraphiQL exposes
 * various React params to enable interesting integrations.
 */
    // Parse the search string to get url parameters.
var search = window.location.search;
var parameters = {};
parameters.variables = awaVariables;
parameters.query = awaQueries;

search.substr(1).split('&').forEach(function (entry) {
    var eq = entry.indexOf('=');
    if (eq >= 0) {
        parameters[decodeURIComponent(entry.slice(0, eq))] =
            decodeURIComponent(entry.slice(eq + 1));
    }
});
// if variables was provided, try to format it.
if (parameters.variables) {
    try {
        parameters.variables =
            JSON.stringify(JSON.parse(parameters.variables), null, 2);
    } catch (e) {
        // Do nothing, we want to display the invalid JSON as a string, rather
        // than present an error.
    }
}
// When the query and variables string is edited, update the URL bar so
// that it can be easily shared
function onEditQuery(newQuery) {
    parameters.query = newQuery;
    updateURL();
}
function onEditVariables(newVariables) {
    parameters.variables = newVariables;
    updateURL();
}
function onEditOperationName(newOperationName) {
    parameters.operationName = newOperationName;
    updateURL();
}
function updateURL() {
    var newSearch = '?' + Object.keys(parameters).filter(function (key) {
        return Boolean(parameters[key]);
    }).map(function (key) {
        return encodeURIComponent(key) + '=' +
            encodeURIComponent(parameters[key]);
    }).join('&');
    history.replaceState(null, null, newSearch);
}
// Defines a GraphQL fetcher using the fetch API. You're not required to
// use fetch, and could instead implement graphQLFetcher however you like,
// as long as it returns a Promise or Observable.
function graphQLFetcher(graphQLParams) {
    // This example expects a GraphQL server at the path /graphql.
    // Change this to point wherever you host your GraphQL server.
    return fetch('/AWAgraphql', {
        method: 'post',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(graphQLParams),
        credentials: 'include',
    }).then(function (response) {
        return response.text();
    }).then(function (responseBody) {
        try {
            return JSON.parse(responseBody);
        } catch (error) {
            return responseBody;
        }
    });
}
// Render <GraphiQL /> into the body.
// See the README in the top level of this module to learn more about
// how you can customize GraphiQL by providing different values or
// additional child elements.
ReactDOM.render(
    React.createElement(GraphiQL, {
        fetcher: graphQLFetcher,
        query: parameters.query,
        variables: parameters.variables,
        operationName: parameters.operationName,
        onEditQuery: onEditQuery,
        onEditVariables: onEditVariables,
        onEditOperationName: onEditOperationName
    }),
    document.getElementById('AWAgraphiql')
);