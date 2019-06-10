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

function initOldVersionWarnings($, thisVersion, projectUrl) {
    if (projectUrl && projectUrl !== "") {
        var schemeLessUrl = projectUrl;
        if (projectUrl.startsWith("http://")) projectUrl = schemeLessUrl.substring(5);
        else if (projectUrl.startsWith("https://")) projectUrl = schemeLessUrl.substring(6);
        const url = schemeLessUrl + (schemeLessUrl.endsWith("\/") ? "" : "/") + "paradox.json";
        $.get(url, function (versionData) {
            const currentVersion = versionData.version;
            if (thisVersion !== currentVersion) {
                showVersionWarning(thisVersion, currentVersion, projectUrl);
            }
        });
    }
}

function showVersionWarning(thisVersion, currentVersion, projectUrl) {
    $('#version-warning').prepend(
        '<div class="callout warning" style="margin-top: 16px">' +
        '<p><span style="font-weight: bold">This documentation regards version ' + thisVersion + ', ' +
        'however the current version is <a href="' + projectUrl + '">' + currentVersion + '</a>.</span></p>' +
        '</div>'
    );
}