package com.lichle.core_common

object ErrorCodes {
    const val NETWORK_ERROR = 1000
    const val TIMEOUT = 1001
    const val UNKNOWN_HOST = 1002
    const val UNAUTHORIZED = 1003
    const val FORBIDDEN = 1004
    const val NOT_FOUND = 1005
    const val SERVER_ERROR = 1006
    const val UNKNOWN_NETWORK_ERROR = 1099

    const val DB_CONSTRAINT_VIOLATION = 2000
    const val DB_CONNECTION_ERROR = 2001
    const val DB_ILLEGAL_STATE = 2002
    const val DB_UNKNOWN_ERROR = 2099

    const val UNKNOWN = 3000
}