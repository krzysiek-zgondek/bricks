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

### What's new in 0.2.0

- New brick: [Entries](#entries) - Simplifies data usage
- Partial documentation

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


#### Work in Progress

Currently description is in progress so if you want more examples
checkout source code and tests.

## Core

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

> **Future improvements**
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

#### KotlinReflect

Contains small ```TClass``` wrapper for ```KClass``` - check source code
KotlinReflect.kt

#### Storage

Unified interface for library:

```kotlin
interface Storage<Id, Type>
```

currently there are two basic implementations:

>    Lists:
>
>    ```kotlin
>    //lists examples
>    val list = listStorage<String>(
>        list = mutableListOf()
>    )
>
>    //synchronized list
>    val synchronized = listStorage<String>(
>        list = Collections.synchronizedList(mutableListOf())
>    )
>    ```
>
>    Maps:
>
>    ```kotlin
>    //maps
>    val map = mapStorage<Any, Any>(map = hashMapOf())
>    ```

## Entries

Simple data usage. Abstract your models from how you store and retrieve
them.

Start developing app

```kotlin
implementation("com.github.krzysiek-zgondek.bricks:entries:$bricksVersion")
implementation("com.github.krzysiek-zgondek.bricks:entries-moshi-integrations:$bricksVersion")
```

#### Usage

Let's say you have ```Profile``` class for holding user data:

```kotlin
data class Profile(
    val id: Long,
    val name: String
)
```

To use Entries you first have to define scope for them. Lets make a one
for this crucial data:

```kotlin
//transcoder is used to mediate data type between storage and scopes
//we will store data in Json
val userScope = scope(transcoder = MoshiTranscoder())
```

Now once we have defined scope we can define our entry:

```kotlin
//our class have no default constructor so we use lambda factory
val profile by entry(userScope){ 
    Profile(id = 1000L, name = "John") 
}
```

now lets use it:

```kotlin
val userScope = scope(transcoder = MoshiTranscoder())

fun main() {
    val profile by entry(userScope) {
        Profile(id = 1000L, name = "John")
    }

    println(profile)
}
```

it also can be modified:

```kotlin
//var instead of val
var profile by entry(userScope) {
    Profile(id = 1000L, name = "John")
}

profile = Profile(id = 500, name = "Renata")
println(profile)
```

#### Entry definitions

Now let's say that ```Person``` class has changed.

```kotlin
//old profile
data class ProfileOld(
    val id: Long,
    val name: String
)

//new profile
data class Profile(
    val id: String,
    val name: String,
    val lastName: String
)
```

If you are using persistent data storage that means old data is stored
in it. To recover entry from old state use entry definitions:

```kotlin
val profileDesc = define(
    factory = {
        Profile(id = "1.2.3.4", name = "John", lastName = "Kowalski")
    },
    archive = archiveOf {
        entry { old: ProfileOld ->
            Profile(id = "${old.id}", name = old.name, lastName = "Kowalski")
        }
    }
)
```

now scope will be able to transform old data into new one:

```kotlin
var profile by entry(userScope, profileDesc)
println(profile)
```

More advanced full example:

```kotlin

/*file:Profile.kt*/
//new profile
data class Profile(
    val id: String,
    val name: String,
    val lastName: String
)


/*file:ProfileEntry*/
//oldest profile
data class ProfileV1(
    val id: Long
)

//old profile
data class ProfileV2(
    val id: Long,
    val name: String
)

/*file:ProfileDescriptor.kt*/
val profileDesc = define<Profile>(
    archive = archiveOf {
        entry { old: ProfileV1 ->
            ProfileV2(id = old.id, name = "John")
        }
        entry { old: ProfileV2 ->
            Profile(id = "${old.id}", name = old.name, lastName = "Kowalski")
        }
    }
)

/*file:ProfileService.kt*/
class ProfileService{
    fun requestProfile(): Profile{
        /*emulate hard work*/
        Thread.sleep(1000)
        return Profile(id = "1.2.3.4", name = "John", lastName = "Kowalski")
    }

    fun logoff(): Profile{
        /*emulate hard work*/
        Thread.sleep(1000)
        return Profile(id = "", name = "", lastName = "")
    }
}

/*file:Application.kt*/
class Application {
    lateinit var service: ProfileService

    private val userScope = scope(transcoder = MoshiTranscoder())

    //entries are lazy by their delegation nature
    private var profile by entry(userScope, profileDesc){
        //this will be called once, only if entry is not already stored in the scope
        service.requestProfile()
    }

    fun main() {
        service = ProfileService()
        //service will be invoke just once
        println("current user is: $profile")

        profile = service.logoff()
        println("current user is: $profile")
    }
}
```

#### Scope Groups

#### Storage

#### Transcoders

#### Loose examples

```kotlin
/*On the fly declaration*/
class SomeClass(scope: EntryScope) {
    /*Custom class*/
    class CustomData

    /*entry delegation*/
    val customEntry = entry<CustomData>(scope)

}

/*Custom class*/
class CustomData

/*Custom entry descriptor, default value by kotlin new instance*/
val CustomDataDescriptor = define<CustomData>()

/*Custom entry descriptor, default value by object construction - use when no empty constructor is provided*/
val CustomDataDescriptor2 = define { CustomData() }

/*Custom entry descriptor, default value by empty constructor*/
val CustomDataDescriptor3 = define(factory = ::CustomData)

/*Custom scope*/
class CustomScope : EntryScope by scope(transcoder = MoshiTranscoder()) {
    /*One entry delegation per scope*/
    val entry = register { CustomDataDescriptor }
    val entry2 = register { define<Person>() }
}

/*Custom group*/
class CustomScopeGroup : EntryScopeGroup by listedScopeGroup() {
    val scope1 = register { CustomScope() }
}
```

> **Future improvements**
> * Observable entries
> * Stable persistent storage
> * Cached storage

