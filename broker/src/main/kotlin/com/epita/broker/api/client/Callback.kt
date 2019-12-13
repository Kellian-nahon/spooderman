package com.epita.broker.api.client

import khttp.responses.Response

typealias Callback = (Response?, Throwable?) -> Unit