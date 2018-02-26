# votocast_dev_sdk

**How to integrate into your app?**



Integrating the library into you app is extremely easy. A few changes in the build gradle and your all ready to use the library. Make the following changes.


Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```


Step 2. Add the dependency

```
dependencies {
    compile 'com.github.ervishu83.votocast_dev_sdk:jcvideoplayer-lib:v1.12'
    compile 'com.github.ervishu83.votocast_dev_sdk:votocast_lib:v1.12'
}
```


**How to use the library?**


Okay seems like you integrated the library in your project but how do you use it? Well its really easy just follows the steps below.
Load native library in your activity on click event of button:

```
      import libClass.votocastLib;
      
      btnVotocast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new votocastLib(MainActivity.this);
            }
        });
 ```
 
# License
 
 ```
Copyright 2017 Votocast.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
