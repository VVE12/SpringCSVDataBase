package com.project.springcsvdatabase.controller;

import com.project.springcsvdatabase.helper.PriceItemHelper;
import com.project.springcsvdatabase.model.PriceItem;
import com.project.springcsvdatabase.response.PriceItemResponse;
import com.project.springcsvdatabase.service.EmailAttachmentService;
import com.project.springcsvdatabase.service.PriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api/csv")
public class PrivateItemController {

    @Autowired
    PriceItemService priceItemService;

    @Autowired
    EmailAttachmentService emailAttachmentService;

    @PostMapping("/get")
    public void getFile(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("directory") String directory) throws IOException, MessagingException {

        emailAttachmentService.downloadEmailAttachments(email, password, directory);
    }

    @PostMapping("/upload")
    public ResponseEntity<PriceItemResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (PriceItemHelper.hasCSVFormat(file)) {
            try {
                priceItemService.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();


                return ResponseEntity.status(HttpStatus.OK).body(new PriceItemResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + ", "
                + "Error: " + e;
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new PriceItemResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PriceItemResponse(message));
    }

    @GetMapping("/items")
    public ResponseEntity<List<PriceItem>> getAllItems() {
        try {
            List<PriceItem> priceItems = priceItemService.getAllItems();

            if (priceItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(priceItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
