package com.epita.bitoduc.api.client

import khttp.responses.Response
import kotlin.Exception

class InvalidStatusException(response: Response): Exception("${response.statusCode} - ${response.text}")
