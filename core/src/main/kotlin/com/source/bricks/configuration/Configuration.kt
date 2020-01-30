package com.source.bricks.configuration

/**
 * Cool thing that would be great to do with configuration package
 * is to enable force load classes which holds references to any configuration instance.
 * In that way it would be easy to auto configure projects by utilizing static blocks of classes
 * and global registry ie. ConfigurationScope which would execute these collected configurations
 * at once.
 *
 * In non android projects it's fairly easy to just scan classpath at the start of the app, but
 * in case of android you would have to unpack apk and scan dex files which i found very
 * difficult.
 *
 * And there is also a proguard thing.
 *
 * The best way i can think of right now is to use gradle tasks and scan dex files right before
 * assembleDebug process and attach results to resources folder. This would work with both
 * proguard enabled and disabled and in case of non-android apps.
 * */


/**
 * Configuration takes [Context] and produces [Result] using [create] method.
 *
 * @see configuration
 * */
interface Configuration<Context, Result> {
    fun create(context: Context): Result
}