package common

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.test.core.app.ApplicationProvider
import org.robolectric.Shadows
import kotlin.reflect.KClass

/**
 * @project Bricks
 * @author SourceOne on 16.01.2020
 */

/**Adds activities to packageManager and enables them to
 * be started without declaring them in AndroidManifest.xml
 * */
fun addTestActivities(vararg activities: KClass<out Activity>) {
    val app = ApplicationProvider.getApplicationContext<Context>()
    val shadowPackageManager = Shadows.shadowOf(app.packageManager)

    activities.forEach { cls ->
        val activityInfo = ActivityInfo().apply {
            name = cls.java.name
            packageName = app.packageName
        }
        shadowPackageManager.addOrUpdateActivity(activityInfo)
    }
}
