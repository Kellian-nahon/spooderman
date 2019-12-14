package com.epita.spooderman.commands

import com.epita.spooderman.types.DocumentContent
import java.net.URL

data class DocumentizeContentCommand(val content: DocumentContent, val url: URL)
