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

You can also leave BEEP message with pull request.

### BEEP - Bricks Evolution and Enhancement Process

BEEP is exactly the same thing as for kotlin KEEPs are. **But it sounds
cooler**.

### Overview

Any module name is described by this template:

>**\[platform\]-\[group\]-\[core\]-\[integrations\]**

* **platform** - is self explanatory, if code is platform independent
  then this is omitted
* **group** - what the module is related to ie. activity, fragment, koin
* **core** - used when code is frequently used in other modules in the
  group
* **integrations** - used when code is related to other modules but is
  either platform dependent or specific library dependent

##### Version

```kotlin
val bricksVersion = "0.2.0"  
```

by the way the version is meaningless, given the nature of this project

## Store


Our hardware store currently proudly contains:
* General:
  * [`core`](#core) - base, configuration, tclass, storage
  * `koin-integrations` - koin configuration integration
  * `test` - small extensions
* Data
  * [`entires`](#entries) - delegation for any data you like
* Android:
  * `android-core` - base, bundle, configuration, intent
  * `android-activity` - arguments
  * `android-fragment` - arguments, switch, observers
  * `android-livedata` - observeOnce
  * `android-resources` - dimensions, display
  * `android-view` - scanning, easier operations

### Core

Foundation for other modules. Defines all base structures. Mostly
contains interfaces and basic structures

```kotlin
implementation("com.github.krzysiek-zgondek.bricks:core:$bricksVersion")
```

#### Configuration

Describes how to create and configure object.

```kotlin
interface Configuration<Context, Result> {
    fun create(context: Context): Result
}
```

Basic usage is shown in this example:

```kotlin
//definition
//configuration that returns result
val mainStorageConfiguration = configuration<Application, Storage> {
    obtainStorageByName("main.storage")
}

//configuration that returns nothing
val dbConfiguration = configuration<Application, Unit> {
    configureLocalDB()
}

//...

//usage
fun main(app: Application){
    val storage = mainStorageConfiguration.create(app)
    val item = storage.get("image.png")
}

```

You can also create configuration scopes to aggregate configurations and
execute it at once:

```kotlin
//file: SomeFile.kt
//scope definition
val DefaultScope by lazy {
    ConfigurationListScope<Application>()
}

//define configurations
val dbConfiguration = configuration(DefaultScope) { println("db")/*...*/ }
val firebaseConfiguration = configuration(DefaultScope) { println("fb")/*...*/ }
val messagingConfiguration = configuration(DefaultScope) {println("msg") /*...*/ }
```

or:

```kotlin
//file: SomeFile.kt
//scope definition
val DefaultScope = ConfigurationListScope<Application>(
    configuration { println("db")/*...*/ },
    configuration { println("fb")/*...*/ },
    configuration { println("msg") /*...*/ }
)
```

then:

```kotlin
//other file
class Application

fun main(app: Application) {
    //execute all configurations in the scope
    DefaultScope.configure { app }
}
```

or if they are spread in different files (until gradle scan plugin is
not written):

```kotlin
//somewhere in db module...
val dbConfiguration = configuration<Application, Unit> { println("db")/*...*/ }
//somewhere in firebase module...
val firebaseConfiguration = configuration<Application, Unit> { println("fb")/*...*/ }
//somewhere in messaging module...
val messagingConfiguration = configuration<Application, Unit> { println("msg") /*...*/ }

//otherfile.kt
//scope definition
val DefaultScope = ConfigurationListScope(
    dbConfiguration, firebaseConfiguration, messagingConfiguration
)

class Application

fun main(app: Application) {
    //execute all configurations in the scope
    DefaultScope.cofigure { app }
}
```

> **TODO**
> * Create Gradle task that scans for every Configuration interface
>   implementation and packs created list so that all it can be force
>   loaded to fill scopes on the fly.
>
>   ```kotlin
>   //core module
>   val DefaultScope = ConfigurationListScope<Application>()
>
>   //somewhere in db module...
>   val dbConfiguration = configuration(DefaultScope){ println("db")/*...*/ }
>
>   //otherfile.kt
>   class Application
>
>   fun main(app: Application) {
>       //execute all configurations in the scope
>       DefaultScope.configure { app }
>   }
>   ```


### Entries

```kotlin
implementation("com.github.krzysiek-zgondek.bricks:entries:$bricksVersion")
```

Todo descriptions


