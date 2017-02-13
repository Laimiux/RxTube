RxTube
============

A Reactive wrapper around Google's YouTube Data API

Example
-
```java
YouTube youTube = initYouTube();
RxTube rxTube = new RxTube(youTube, browserKey);
```


```java
final String videoId = "iX-QaNzd-0Y";
rxTube.create(new RxTube.Query<YouTube.Videos.List>() {
  @Override public YouTube.Videos.List create(YouTube youTube) throws Exception {
    final YouTube.Videos.List query = youTube.videos().list("snippet");
    query.setId(videoId);
    return query;
  }
});
```


Download
-
```groovy
compile 'com.laimiux.rxtube:rxtube:0.0.2'
```

Sample
-

A simple list of videos that you can watch by clicking on it.

To build it, add `youtube.properties` to sample directory with
```
youtubeDeveloperKey=EDIT_THIS
youtubeBrowserDevKey=EDIT_THIS
```

![ListView](website/list.png)


License
-------

    Copyright 2014 Laimonas Turauskas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


