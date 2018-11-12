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

!function(e,i){"function"==typeof define&&define.amd?define(i):"object"==typeof exports?module.exports=i():i()(e.lunr)}(this,function(){return function(e){e.multiLanguage=function(){for(var i=Array.prototype.slice.call(arguments),t=i.join("-"),r="",n=[],s=[],p=0;p<i.length;++p)"en"==i[p]?(r+="\\w",n.unshift(e.stopWordFilter),n.push(e.stemmer),s.push(e.stemmer)):(r+=e[i[p]].wordCharacters,n.unshift(e[i[p]].stopWordFilter),n.push(e[i[p]].stemmer),s.push(e[i[p]].stemmer));var o=e.trimmerSupport.generateTrimmer(r);return e.Pipeline.registerFunction(o,"lunr-multi-trimmer-"+t),n.unshift(o),function(){this.pipeline.reset(),this.pipeline.add.apply(this.pipeline,n),this.searchPipeline&&(this.searchPipeline.reset(),this.searchPipeline.add.apply(this.searchPipeline,s))}}}});