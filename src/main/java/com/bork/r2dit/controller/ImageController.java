package com.bork.r2dit.controller;

import com.bork.r2dit.entity.Image;
import com.bork.r2dit.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable Long id) throws IOException {
        Image image = imageRepository.findById(id).get();
        return image.getData();
    }
}