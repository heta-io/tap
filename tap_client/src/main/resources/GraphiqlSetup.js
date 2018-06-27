/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

import React from 'react';
import ReactDOM from 'react-dom';
import GraphiQL from 'graphiql';
import fetch from 'isomorphic-fetch';

export function myFunc(input) {
    return "This is from graphiqlsetup.js - your text: "+input;
}

export function load(graphQlQuery,graphQlVariables) {

    //console.log("load function has been called!");

    function graphQLFetcher(graphQLParams) {
        return fetch(window.location.origin + '/graphql', {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(graphQLParams),
        }).then(response => response.json());
    }

    ReactDOM.render(
        React.createElement(GraphiQL, {
            fetcher: graphQLFetcher,
            query: graphQlQuery,
            variables: graphQlVariables
        }),
        document.getElementById('graphiql')
    );

    return "GraphiQL javascript loaded";
}