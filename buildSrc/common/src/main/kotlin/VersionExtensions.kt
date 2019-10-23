import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

var Project.majorVersion: Int
    get() = rootProject.extra["version.major"] as Int
    set(value) {
        rootProject.extra["version.major"] = value
    }
var Project.minorVersion: Int
    get() = rootProject.extra["version.minor"] as Int
    set(value) {
        rootProject.extra["version.minor"] = value
    }

var Project.stepVersion: Int
    get() = rootProject.extra["version.step"] as Int
    set(value) {
        rootProject.extra["version.step"] = value
    }

val Project.`version-full`: String
    get() = "$majorVersion.$minorVersion.$stepVersion"
