package com.epita.crawler

import com.epita.crawler.cleaners.Cleaner
import com.epita.crawler.core.Crawler
import com.epita.crawler.fetcher.Fetcher
import com.epita.crawler.urlseeker.UrlSeeker

class DefaultCrawler(override val fetcher: Fetcher,
                     override val cleaner: Cleaner,
                     override val urlSeeker: UrlSeeker) : Crawler {

}