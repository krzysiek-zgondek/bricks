package com.source.bricks.activity.argument

import com.source.core.provider.DelegateProvider


/**
 * For clarity only
 * May be removed in the future
 * */
interface ActivityArgumentProvider<Type> :
    DelegateProvider<Any, ActivityArgument<Type>>