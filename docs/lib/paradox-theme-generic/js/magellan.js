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

$(function() {

  // add magellan targets to anchor headers, up to depth 3
  $("a.anchor").each(function() {
    var anchor = $(this);
    var name = anchor.attr("name");
    var header = anchor.parent();
    if (header.is("h1") || header.is("h2") || header.is("h3")) {
      header.attr("id", name).attr("data-magellan-target", name);
    }
  });

  // enable magellan plugin on the page navigation
  $(".page-nav").each(function() {
    var nav = $(this);

    // strip page navigation links down to just the hash fragment
    nav.find("a").attr('href', function(_, current){
        return this.hash ? this.hash : current;
    });

    new Foundation.Magellan(nav);
  });

});
