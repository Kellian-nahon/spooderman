package com.epita.indexer.tokenisation

import java.util.function.Function

interface WordTransformer: Function<String, String?>