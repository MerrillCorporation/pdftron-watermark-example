package com.pdftron.watermarking.demo;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DownloadController {

    private DownloadService downloadService;
    private WatermarkService watermarkService;

    public DownloadController(DownloadService downloadService, WatermarkService watermarkService) {
        this.downloadService = downloadService;
        this.watermarkService = watermarkService;
    }

    @GetMapping
    public String homePage() {
        return "home";
    }

    @GetMapping("/unwatermarked")
    public void downloadUnwatermarked(HttpServletResponse response) throws IOException {
        IOUtils.copy(downloadService.getFile("Hello_world.pdf"), response.getOutputStream());
    }

    @GetMapping("/watermarked")
    public void downloadWatermarked(HttpServletResponse response) throws IOException {
        watermarkService.watermarkFile(response.getOutputStream(),"Hello_world.pdf");
    }
}
