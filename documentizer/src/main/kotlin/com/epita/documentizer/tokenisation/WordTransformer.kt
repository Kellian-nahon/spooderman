package com.epita.documentizer.tokenisation

import java.util.function.Function

interface WordTransformer: Function<String, String?>