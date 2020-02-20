[![](https://jitpack.io/v/krzysiek-zgondek/bricks.svg)](https://jitpack.io/#krzysiek-zgondek/bricks)

### What is Bricks

A collection of code to ease our daily tasks of building and maintaining
apps.

It's packed in small submodules so you can grab only that much as you
need. Just like in your favourite Hardware Store.

### Vision

I would like this library to contain "screws, bolts and nuts" that we
all need to build apps faster and better.  
But also to be a product of people collaboration. So if you have code
you think is good to be recycled from project to project over and over
again, don't hesitate, leave a BEEP here and share.

Later on i will set up some kind of medium so that we can communicate
but currently if you want to join this hassle, drop me an email at
>krzysiek.zgondek@gmail.com.

### BEEP - Bricks Evolution and Enhancement Process

BEEP is exactly the same thing as for kotlin KEEPs are. **But it sounds
cooler**.

### Overview

Any module name is described by this template:

>**\[platform\]-\[group\]-\[core\]-\[integrations\]**

* **platform** - is self explanatory, if code is platform independent
  then this is omitted
* **group** - what the module is related to ie. activity, fragment, koin
* **core** - used when code is frequently used in other modules in the group
* **integrations** - used when code is related to other modules but
  is either platform dependent or specific library dependent

## Store

```kotlin
val bricksVersion = "0.1.2"  
 ```

Our hardware store currently proudly contains:
* General:
  * [`core`](#core) - base, configuration, tclass, storage
  * `koin-integrations` - koin configuration integration
  * `test` - small extensions
* Android:
  * `android-core` - base, bundle, configuration, intent
  * `android-activity` - arguments
  * `android-fragment` - arguments, switch, observers
  * `android-livedata` - observeOnce
  * `android-resources` - dimensions, display
  * `android-view` - scanning, easier operations
* Data
  * `entires` - delegation for any data you like

### Core
```kotlin
implementation("com.github.krzysiek-zgondek.bricks:core:$bricksVersion")
 ```

### Entries
Entries module provides

