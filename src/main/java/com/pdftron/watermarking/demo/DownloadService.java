package com.pdftron.watermarking.demo;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DownloadService {
    public InputStream getFile(String file) {
        return this.getClass().getResourceAsStream("/" + file);
    }

    public long getFileSize(String file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IOUtils.copy(getFile(file), baos);
        } catch (IOException e) {
            throw new RuntimeException("failed to load or copy file", e);
        }
        return baos.toByteArray().length;
    }
}
