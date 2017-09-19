$(function (global) {
  /**
   * This GraphiQL example illustrates how to use some of GraphiQL's props
   * in order to enable reading and updating the URL parameters, making
   * link sharing of queries a little bit easier.
   *
   * This is only one example of this kind of feature, GraphiQL exposes
   * various React params to enable interesting integrations.
   */

    // Parse the search string to get url parameters.

  var instructions = "# Welcome to GraphiQL\n" +
    "#\n" +
    "# GraphiQL is an in-browser IDE for writing, validating, and\n" +
    "# testing GraphQL queries.\n" +
    "#\n" +
    "# Type queries into this side of the screen, and you will\n" +
    "# see intelligent typeaheads aware of the current GraphQL type schema and\n" +
    "# live syntax and validation errors highlighted within the text.\n" +
    "#\n" +
    "# To bring up the auto-complete at any point, just press Ctrl-Space.\n" +
    "#\n" +
    "# Press the run button above, or Cmd-Enter to execute the query, and the result\n" +
    "# will appear in the pane to the right.\n\n" +
    "\n" +
    "# Text Analytics Pipeline\n" +
    "\n"

  var exampleQueries = "query CleanText($input: String!) {\n" +
    "  clean(text:$input) {\n" +
    "    analytics\n" +
    "    timestamp\n" +
    "  }\n" +
    "  cleanPreserve(text:$input) {\n" +
    "    analytics\n" +
    "  }\n" +
    "  cleanMinimal(text:$input) {\n" +
    "    analytics\n" +
    "  }\n" +
    "  cleanAscii(text:$input) {\n" +
    "    analytics\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "query MakeVisible($input: String!) {\n" +
    "  visible(text:$input) {\n" +
    "    analytics\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "query Sentences($input: String!) {\n" +
    "  sentences(text:$input) {\n" +
    "    analytics {\n" +
    "      start\n" +
    "      end\n" +
    "      length\n" +
    "      tokens {\n" +
    "        term\n" +
    "        lemma\n" +
    "        postag\n" +
    "        parent\n" +
    "        child\n" +
    "        deptype\n" +
    "      }\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "\n" +
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
    "\n" +
    "query Metrics($input: String!) {\n" +
    "  metrics(text:$input) {\n" +
    "    analytics {\n" +
    "      wordCount\n" +
    "    }\n" +
    "    timestamp\n" +
    "  }\n" +
    "}\n" +
    "\n" +
    "query Athanor($input: String!) {\n" +
    "  moves(text:$input) {\n" +
    "    analytics\n" +
    "  }\n" +
    "}\n" +
    "\n"

    var exampleVariables = "{\"input\": \"I didn't take any time to review the subject outline nor did I log onto UTS Online to review any supporting information to provide context, I walked into class like a blank canvas. I had no idea what this course was about but I was certain it had something to do with responsibility and leaders. I reflected on this and felt decision making was like second nature, yes I over-thought my decisions whether it was personal or professional but I never thought of the act of having to justify my decisions.\"}"



  var search = window.location.search;
  var parameters = {
    variables: exampleVariables
  };
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
        JSON.stringify(JSON.parse(query.variables), null, 2);
    } catch (e) {
      // Do nothing
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

  function updateURL() {
    var newSearch = '?' + Object.keys(parameters).map(function (key) {
      return encodeURIComponent(key) + '=' +
        encodeURIComponent(parameters[key]);
    }).join('&');
    history.replaceState(null, null, newSearch);
  }

  // Defines a GraphQL fetcher using the fetch API.
  function graphQLFetcher(graphQLParams) {
    return fetch(window.location.origin + '/graphql', {
      method: 'post',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(graphQLParams),
      credentials: 'include'
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

  function setupZoom(percent) {
    $('html > head').append($('<style>body {zoom: ' + percent + '%;}</style>'))
  }

  if (parameters['zoom']) {
    setupZoom(parameters['zoom'])
  }

  if (parameters["hideVariables"]) {
    $('html > head').append($('<style>.variable-editor {display: none !important}</style>'))
  }



  global.renderGraphiql = function (elem) {
    // Render <GraphiQL /> into the body.
    ReactDOM.render(
      React.createElement(GraphiQL, {
        fetcher: graphQLFetcher,
        query: parameters.query,
        variables: parameters.variables,
        response: parameters.response,
        onEditQuery: onEditQuery,
        onEditVariables: onEditVariables,
        defaultQuery: instructions + exampleQueries
      }),
      elem
    );
  }
}(window))