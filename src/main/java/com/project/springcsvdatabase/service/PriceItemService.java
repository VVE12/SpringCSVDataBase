package com.project.springcsvdatabase.service;

import com.project.springcsvdatabase.helper.PriceItemHelper;
import com.project.springcsvdatabase.model.PriceItem;
import com.project.springcsvdatabase.repository.PriceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class PriceItemService {

    @Autowired
    PriceItemRepository priceItemRepository;

    public void save(MultipartFile file) {
        try {
            List<PriceItem> priceItems = PriceItemHelper.csvToPriceItems(file.getInputStream());
            priceItemRepository.saveAll(priceItems);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<PriceItem> getAllItems() {
        return priceItemRepository.findAll();
    }

}
