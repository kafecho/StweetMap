/*
 * Copyright 2009 Guillaume Belrose
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
*/

StweetMap is a GoogleMap / Twitter search mashup that is written entirely using the Google Web Toolkit.
The code is packaged up as an Eclipse project using the GWT plugin ( http://code.google.com/eclipse/ ).
You can run the code on your computer, or you can upload it to Google App Engine. There is a hosted version running at http://stweetmap.appspot.com/

What does it do?
---------------
StweetMap lets you enter a search topic and see the location of Twitter users who've been writing messages about it.
It uses the GoogleMap geocoding service http://code.google.com/apis/maps/documentation/v3/services.html#Geocoding.
It can also parse iPhone location information contained in a user profile.

TODOs
-----
Extract Twitter geo-location data from tweet messages.
Parse Foursquare related tweets.
Use a semantic tagging service such as OpenCalais to extract location data from tweets. 




