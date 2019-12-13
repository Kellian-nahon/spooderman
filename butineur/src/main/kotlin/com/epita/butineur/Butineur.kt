package com.epita.butineur

import com.epita.butineur.cleaners.Cleaner
import com.epita.butineur.core.Crawler
import com.epita.butineur.fetcher.Fetcher
import com.epita.butineur.urlseeker.UrlSeeker

class Butineur(override val fetcher: Fetcher,
               override val cleaner: Cleaner,
               override val urlSeeker: UrlSeeker) : Crawler {

}