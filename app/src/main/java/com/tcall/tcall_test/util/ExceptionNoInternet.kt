package com.tcall.tcall_test.util

import java.io.IOException

class ExceptionNoInternet(msg: String?, cause: Throwable?) : IOException(msg, cause)