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
    compile 'com.github.ervishu83.votocast_dev_sdk:jcvideoplayer-lib:v1.8'
    compile 'com.github.ervishu83.votocast_dev_sdk:votocast_lib:v1.8'
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
 
