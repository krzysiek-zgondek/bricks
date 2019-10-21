plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins{
        register("plugin2"){
            id = "plugin2"
            implementationClass = "Plugin2"
        }
    }
}