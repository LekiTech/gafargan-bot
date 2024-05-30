package core.database.service;

import core.database.entity.Search;
import core.database.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public void saveSearch(Search search) {
        searchRepository.save(search);
    }
}
