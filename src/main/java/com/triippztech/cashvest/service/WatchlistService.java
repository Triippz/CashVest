/*
 * Copyright 2020 Mark Tripoli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.triippztech.cashvest.service;

import com.triippztech.cashvest.domain.Portfolio;
import com.triippztech.cashvest.domain.Watchlist;
import com.triippztech.cashvest.repository.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistService {

    private final Logger log = LoggerFactory.getLogger(WatchlistService.class);

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public Watchlist save(Watchlist watchlist) {
        log.debug("Request to save Watchlist: {}", watchlist);
        return watchlistRepository.save(watchlist);
    }

    public Watchlist createWatchlist(Watchlist watchlist) throws Exception {
        log.debug("Request to create Watchlist: {}", watchlist);
        Optional<Watchlist> foundWatchlist =
                watchlistRepository.findByListNameAndWatchlistUser(watchlist.getListName(), watchlist.getWatchlistUser());
        if ( foundWatchlist.isPresent() )
            throw new Exception("Portfolio with that name already exists");

        watchlistRepository.save(watchlist);
        return watchlist;
    }

    public void delete(Watchlist watchlist) {
        log.debug("Request to delete Watchlist:{}", watchlist);
        watchlistRepository.deleteById(watchlist.getId());
    }
}
