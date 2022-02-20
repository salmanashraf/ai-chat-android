package com.sa.mudah.chatmessenger.output

open class BaseOutput<E> {
    val error by lazy { SingleLiveEvent<E>() }

}