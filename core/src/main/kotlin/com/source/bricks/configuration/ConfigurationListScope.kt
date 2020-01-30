package com.source.bricks.configuration


/**
 * [ConfigurationScope] implementation using [List] as a registry for collected
 * configurations.
 *
 * You can initialize this scope with collection of configurations using its constructor.
 * Using [configure] you can setup scope before executing all configurations.
 * Executions is always in the same order as configurations where added to scope.
 * */
class ConfigurationListScope<Context>(
    vararg configurations: Configuration<Context, out Any?>
) : ConfigurationScope<Context> {

    private val list: MutableList<Configuration<Context, out Any?>> = mutableListOf()

    init {
        configurations.forEach { configuration ->
            add(configuration)
        }
    }

    override fun <Result> add(configuration: Configuration<Context, Result>) {
        list += configuration
    }

    override fun configure(initializer: ConfigurationScope<Context>.() -> Context) {
        val context = initializer()
        list.forEach { configuration ->
            configuration.use(context)
        }
    }
}

