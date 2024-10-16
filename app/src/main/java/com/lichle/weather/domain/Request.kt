package com.lichle.weather.domain

open class Request<Request>(
    val data: Request,
)

object NoRequest : Request<Unit>(Unit)
